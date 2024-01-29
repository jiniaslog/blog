package kr.co.jiniaslog.user.application

import kr.co.jiniaslog.shared.core.annotation.UseCaseInteractor
import kr.co.jiniaslog.user.application.infra.AuthTokenInfo
import kr.co.jiniaslog.user.application.infra.AuthUserLockManager
import kr.co.jiniaslog.user.application.infra.ProviderResolver
import kr.co.jiniaslog.user.application.infra.TokenStore
import kr.co.jiniaslog.user.application.infra.UserAuthTransactionHandler
import kr.co.jiniaslog.user.application.infra.UserRepository
import kr.co.jiniaslog.user.application.usecase.IGetOAuthRedirectionUrl
import kr.co.jiniaslog.user.application.usecase.IRefreshToken
import kr.co.jiniaslog.user.application.usecase.ISignInOAuthUser
import kr.co.jiniaslog.user.application.usecase.UseCasesUserAuthFacade
import kr.co.jiniaslog.user.domain.auth.provider.ProviderUserInfo
import kr.co.jiniaslog.user.domain.auth.token.RefreshToken
import kr.co.jiniaslog.user.domain.auth.token.TokenManger
import kr.co.jiniaslog.user.domain.user.User
import kr.co.jiniaslog.user.domain.user.UserId
import mu.KotlinLogging
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

private val log = KotlinLogging.logger {}

@UseCaseInteractor
class UserAuthService(
    private val userRepository: UserRepository,
    private val tokenStore: TokenStore,
    private val providerResolver: ProviderResolver,
    private val tokenManger: TokenManger,
    private val transactionHandler: UserAuthTransactionHandler,
    private val idempotencyLockManager: AuthUserLockManager,
) : UseCasesUserAuthFacade {
    override fun handle(command: IGetOAuthRedirectionUrl.Command): IGetOAuthRedirectionUrl.Info =
        with(command) {
            require(providerResolver.isSupported(provider)) { "not supported provider. provider: $provider" }
            val providerAdapter = providerResolver.resolve(provider)
            val loginUrl = providerAdapter.getLoginUrl()
            return IGetOAuthRedirectionUrl.Info(loginUrl)
        }

    override fun handle(command: ISignInOAuthUser.Command): ISignInOAuthUser.Info =
        with(command) {
            val providerAdapter = providerResolver.resolve(provider)
            val providerUserInfo = providerAdapter.getUserInfo(code)

            val info =
                transactionHandler.runInRepeatableReadTransaction {
                    val user = getOrCreateUser(providerUserInfo)
                    val accessToken = tokenManger.generateAccessToken(user.id, user.roles)
                    val refreshToken = tokenManger.generateRefreshToken(user.id, user.roles)
                    tokenStore.save(user.id, accessToken, refreshToken)

                    return@runInRepeatableReadTransaction ISignInOAuthUser.Info(
                        accessToken = accessToken,
                        refreshToken = refreshToken,
                        nickName = user.nickName,
                        email = user.email,
                        roles = user.roles,
                        picUrl = providerUserInfo.picture,
                    )
                }
            return info
        }

    private fun getOrCreateUser(providerUserInfo: ProviderUserInfo): User =
        userRepository.findByEmail(providerUserInfo.email)?.let {
            it.refreshWith(providerUserInfo.nickName)
            userRepository.save(it)
        } ?: let {
            val newUser = User.newOne(providerUserInfo.nickName, providerUserInfo.email)
            userRepository.save(newUser)
        }

    override fun handle(command: IRefreshToken.Command): IRefreshToken.Info =
        with(command) {
            require(tokenManger.validateToken(refreshToken)) { "invalid refresh token" }
            val userId = tokenManger.extractUserId(refreshToken)
            val storedAuthToken = tokenStore.findByUserId(userId)
            validateRefreshToken(storedAuthToken, refreshToken)

            val isRecentlyIssued = storedAuthToken.oldRefreshToken == refreshToken
            if (isRecentlyIssued) {
                log.info { "recently issued refresh token. userId: $userId" }
                return IRefreshToken.Info(
                    accessToken = storedAuthToken.accessToken,
                    refreshToken = storedAuthToken.newRefreshToken,
                )
            }

            return idempotencyLockManager.lock(
                userId = userId,
                timeOutSeconds = 10,
                block = {
                    generateNewAuthTokens(refreshToken, userId)
                        .also { log.info { "refreshed auth tokens. userId: $userId" } }
                },
                forIdempotencyFallback = {
                    log.warn {
                        """
                            |already has lock. userId: $userId
                            |refreshToken: $refreshToken
                            |should suspect duplicate request!
                        """.trimMargin()
                    }
                    reTryGetAuthTokensInCache(refreshToken, userId)
                },
            )
        }

    private fun reTryGetAuthTokensInCache(
        refreshToken: RefreshToken,
        userId: UserId,
    ): IRefreshToken.Info {
        val tokens = tokenStore.findByUserId(userId)
        validateRefreshToken(tokens, refreshToken)

        return IRefreshToken.Info(
            accessToken = tokens.accessToken,
            refreshToken = tokens.newRefreshToken,
        )
    }

    private fun generateNewAuthTokens(
        refreshToken: RefreshToken,
        userId: UserId,
    ): IRefreshToken.Info {
        val foundAuthTokens = tokenStore.findByUserId(userId)
        validateRefreshToken(foundAuthTokens, refreshToken)
        val roles = tokenManger.getRole(refreshToken)
        val newAccessToken = tokenManger.generateAccessToken(userId, roles)
        val newRefreshToken = tokenManger.generateRefreshToken(userId, roles)
        transactionHandler.runInRepeatableReadTransaction {
            tokenStore.save(userId, newAccessToken, newRefreshToken)
        }
        return IRefreshToken.Info(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken,
        )
    }

    @OptIn(ExperimentalContracts::class)
    private fun validateRefreshToken(
        foundAuthTokens: AuthTokenInfo?,
        refreshToken: RefreshToken,
    ) {
        contract { returns() implies (foundAuthTokens != null) }
        requireNotNull(foundAuthTokens) { "refresh token should be managed in store" }
        require(
            foundAuthTokens.newRefreshToken == refreshToken ||
                foundAuthTokens.oldRefreshToken == refreshToken,
        ) { "invalid refresh token" }
    }
}

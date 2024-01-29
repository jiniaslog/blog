package kr.co.jiniaslog.user.application.usecase

interface UseCasesUserAuthFacade :
    IGetOAuthRedirectionUrl,
    ISignInOAuthUser,
    IRefreshToken

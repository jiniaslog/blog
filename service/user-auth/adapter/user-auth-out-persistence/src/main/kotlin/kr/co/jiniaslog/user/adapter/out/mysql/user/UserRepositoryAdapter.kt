package kr.co.jiniaslog.user.adapter.out.mysql.user

import kr.co.jiniaslog.shared.core.annotation.PersistenceAdapter
import kr.co.jiniaslog.user.application.infra.UserRepository
import kr.co.jiniaslog.user.domain.user.Email
import kr.co.jiniaslog.user.domain.user.User
import kr.co.jiniaslog.user.domain.user.UserId

@PersistenceAdapter
internal class UserRepositoryAdapter(
    private val userJpaRepository: UserJpaRepository,
) : UserRepository {
    override fun findByEmail(email: Email): User? {
        return userJpaRepository.findByEmail(email.value)?.toDomain()
    }

    override fun findById(id: UserId): User? {
        return userJpaRepository.findById(id.value).orElse(null)?.toDomain()
    }

    override fun findAll(): List<User> {
        return userJpaRepository.findAll().map { it.toDomain() }
    }

    override fun deleteById(id: UserId) {
        userJpaRepository.deleteById(id.value)
    }

    override fun save(entity: User): User {
        val pm = userJpaRepository.findById(entity.id.value).orElse(null)

        val userJpaEntity =
            pm?.apply {
                email = entity.email.value
                nickName = entity.nickName.value
                roles = entity.roles.joinToString(separator = ",") { it.name }
            } ?: UserJpaEntity(
                id = entity.id.value,
                email = entity.email.value,
                nickName = entity.nickName.value,
                roles = entity.roles.joinToString(separator = ",") { it.name },
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt,
            )

        return userJpaRepository.save(userJpaEntity)
            .toDomain()
    }
}

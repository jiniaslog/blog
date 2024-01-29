package kr.co.jiniaslog.user.fakes

import kr.co.jiniaslog.user.application.infra.UserRepository
import kr.co.jiniaslog.user.domain.user.Email
import kr.co.jiniaslog.user.domain.user.User
import kr.co.jiniaslog.user.domain.user.UserId

class UserFakeRepository : UserRepository {
    private val users = mutableMapOf<UserId, User>()

    override fun findByEmail(email: Email): User? {
        return users.values.find { it.email == email }
    }

    override fun findById(id: UserId): User? {
        return users[id]
    }

    override fun findAll(): List<User> {
        return users.values.toList()
    }

    override fun deleteById(id: UserId) {
        users.remove(id)
    }

    override fun save(entity: User): User {
        users[entity.id] = entity
        return entity
    }

    fun tearDown() {
        users.clear()
    }
}

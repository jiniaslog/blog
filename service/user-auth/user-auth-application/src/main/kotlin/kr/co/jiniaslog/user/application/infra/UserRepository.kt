package kr.co.jiniaslog.user.application.infra

import kr.co.jiniaslog.shared.core.domain.Repository
import kr.co.jiniaslog.user.domain.user.Email
import kr.co.jiniaslog.user.domain.user.User
import kr.co.jiniaslog.user.domain.user.UserId

interface UserRepository : Repository<User, UserId> {
    fun findByEmail(email: Email): User?
}

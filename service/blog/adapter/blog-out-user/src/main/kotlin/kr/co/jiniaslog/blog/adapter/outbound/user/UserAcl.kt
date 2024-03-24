package kr.co.jiniaslog.blog.adapter.outbound.user

import kr.co.jiniaslog.blog.domain.user.UserId
import kr.co.jiniaslog.blog.outbound.UserService
import kr.co.jiniaslog.shared.core.annotation.CustomComponent
import kr.co.jiniaslog.user.adapter.inbound.acl.UserAclInboundAdapter

@CustomComponent
class UserAcl(
    private val userQueries: UserAclInboundAdapter,
) : UserService {
    override fun isExistUser(id: UserId): Boolean {
        return userQueries.isExistUser(id.value)
    }
}

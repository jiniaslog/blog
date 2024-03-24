package kr.co.jiniaslog.blog.outbound

import kr.co.jiniaslog.blog.domain.user.UserId

interface UserService {
    fun isExistUser(id: UserId): Boolean
}

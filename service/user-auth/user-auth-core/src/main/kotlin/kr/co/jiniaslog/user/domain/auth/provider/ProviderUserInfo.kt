package kr.co.jiniaslog.user.domain.auth.provider

import kr.co.jiniaslog.shared.core.domain.vo.Url
import kr.co.jiniaslog.shared.core.domain.vo.ValueObject
import kr.co.jiniaslog.user.domain.user.Email
import kr.co.jiniaslog.user.domain.user.NickName

data class ProviderUserInfo(
    val nickName: NickName,
    val email: Email,
    val picture: Url?,
    val provider: Provider,
) : ValueObject {
    override fun validate() {}
}

package kr.co.jiniaslog.user.domain.auth.token

import kr.co.jiniaslog.shared.core.domain.vo.ValueObject

interface AuthToken : ValueObject {
    val value: String
}

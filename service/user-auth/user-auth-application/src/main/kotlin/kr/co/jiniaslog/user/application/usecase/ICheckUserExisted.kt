package kr.co.jiniaslog.user.application.usecase

import kr.co.jiniaslog.user.domain.user.UserId

interface ICheckUserExisted {
    fun handle(command: Command): Boolean

    data class Command(
        val id: UserId,
    )
}

package kr.co.jiniaslog.memo.adapter.inbound.websocket

import jakarta.validation.Valid
import kr.co.jiniaslog.memo.usecase.IUpdateMemo
import kr.co.jiniaslog.memo.usecase.UseCasesMemoFacade
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller

private const val MEMO_PROTOCOL = "/topic/memoResponse"
private val log = mu.KotlinLogging.logger { }

@Controller
class MemoWebSocketHandler(
    private val memoUseCases: UseCasesMemoFacade,
) {
    @MessageMapping("/updateMemo")
    @SendTo(MEMO_PROTOCOL)
    fun handle(
        @Valid payload: UpdateMemoPayload,
    ): UpdateMemoResponse {
        val command: IUpdateMemo.Command = payload.toCommand()
        return memoUseCases.handle(command)
            .toResponse()
    }

    @MessageMapping("/updateReferences")
    @SendTo("$MEMO_PROTOCOL/updateReferences")
    fun handle(
        @Valid payload: UpdateReferencesPayload,
    ): UpdateReferencesResponse {
        val command: IUpdateMemo.Command = payload.toCommand()
        return UpdateReferencesResponse.from(memoUseCases.handle(command).id.value)
    }
}

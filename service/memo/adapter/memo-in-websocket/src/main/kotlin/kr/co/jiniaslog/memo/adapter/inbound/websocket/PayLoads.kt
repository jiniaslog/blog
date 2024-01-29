package kr.co.jiniaslog.memo.adapter.inbound.websocket

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import jakarta.validation.constraints.NotEmpty
import kr.co.jiniaslog.memo.domain.memo.MemoContent
import kr.co.jiniaslog.memo.domain.memo.MemoId
import kr.co.jiniaslog.memo.domain.memo.MemoTitle
import kr.co.jiniaslog.memo.usecase.IUpdateMemo

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type",
)
@JsonSubTypes(
    JsonSubTypes.Type(value = UpdateMemoPayload::class, name = "UpdateMemo"),
    JsonSubTypes.Type(value = UpdateReferencesPayload::class, name = "UpdateReferences"),
)
sealed class PayLoad {
    abstract val type: String
}

data class UpdateMemoPayload(
    override val type: String = "UpdateMemo",
    val id: Long,
    @field:NotEmpty
    val content: String,
    @field:NotEmpty
    val title: String,
) : PayLoad() {
    fun toCommand(): IUpdateMemo.Command.UpdateForm {
        return IUpdateMemo.Command.UpdateForm(
            content = MemoContent(content),
            title = MemoTitle(title),
            memoId = MemoId(id),
        )
    }
}

data class UpdateMemoResponse(
    val id: Long,
) {
    companion object {
        fun from(id: Long): UpdateMemoResponse {
            return UpdateMemoResponse(
                id = id,
            )
        }
    }
}

fun IUpdateMemo.Info.toResponse(): UpdateMemoResponse {
    return UpdateMemoResponse.from(id.value)
}

data class UpdateReferencesPayload(
    override val type: String = "UpdateReferences",
    val id: Long,
    val references: List<Long>,
) : PayLoad() {
    fun toCommand(): IUpdateMemo.Command {
        return IUpdateMemo.Command.UpdateReferences(
            memoId = MemoId(id),
            references = references.map { MemoId(it) }.toSet(),
        )
    }
}

data class UpdateReferencesResponse(
    val id: Long,
) {
    companion object {
        fun from(id: Long): UpdateReferencesResponse {
            return UpdateReferencesResponse(
                id = id,
            )
        }
    }
}

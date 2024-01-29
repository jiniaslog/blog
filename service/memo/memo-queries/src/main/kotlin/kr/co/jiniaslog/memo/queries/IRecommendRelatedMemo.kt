package kr.co.jiniaslog.memo.queries

import kr.co.jiniaslog.memo.domain.memo.MemoContent
import kr.co.jiniaslog.memo.domain.memo.MemoId
import kr.co.jiniaslog.memo.domain.memo.MemoTitle

interface IRecommendRelatedMemo {
    fun handle(query: Query): Info

    data class Query(
        private val _keyword: String,
        val thisMemoId: MemoId,
    ) {
        val keyword: String
            get() = escapeSpecialCharacters()

        private fun escapeSpecialCharacters(): String {
            return _keyword
                .replace("\\", "\\\\") // 역슬래시
                .replace("(", "\\(") // 여는 괄호
                .replace(")", "\\)") // 닫는 괄호
                .replace("\"", "\\\"") // 따옴표
                .replace("'", "\\'") // 작은따옴표
        }

        init {
            require(keyword.isNotBlank()) { "keyword should not be blank" }
        }
    }

    data class Info(
        val relatedMemoCandidates: List<Triple<MemoId, MemoTitle, MemoContent>>,
    )
}

package kr.co.jiniaslog.memo.outbound

import kr.co.jiniaslog.memo.domain.memo.Memo
import kr.co.jiniaslog.memo.domain.memo.MemoId
import kr.co.jiniaslog.shared.core.domain.Repository

interface MemoRepository : Repository<Memo, MemoId> {
    fun findByRelatedMemo(keyword: String): List<Memo>
}

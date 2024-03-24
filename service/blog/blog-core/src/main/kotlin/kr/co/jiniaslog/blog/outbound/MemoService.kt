package kr.co.jiniaslog.blog.outbound

import kr.co.jiniaslog.blog.domain.memo.MemoId

interface MemoService {
    fun isExistMemo(id: MemoId): Boolean
}

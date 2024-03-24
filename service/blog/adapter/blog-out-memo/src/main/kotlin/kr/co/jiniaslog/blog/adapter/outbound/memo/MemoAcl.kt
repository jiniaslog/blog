package kr.co.jiniaslog.blog.adapter.outbound.memo

import kr.co.jiniaslog.blog.domain.memo.MemoId
import kr.co.jiniaslog.blog.outbound.MemoService
import kr.co.jiniaslog.memo.adapter.inbound.acl.MemoAclInboundAdapter
import kr.co.jiniaslog.shared.core.annotation.CustomComponent

@CustomComponent
class MemoAcl(
    private val memoQueries: MemoAclInboundAdapter,
) : MemoService {
    override fun isExistMemo(id: MemoId): Boolean {
        return memoQueries.isExistMemo(id.value)
    }
}

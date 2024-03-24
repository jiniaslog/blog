package kr.co.jiniaslog.memo.adapter.inbound.acl

import kr.co.jiniaslog.memo.domain.memo.MemoId
import kr.co.jiniaslog.memo.queries.ICheckMemoExisted
import kr.co.jiniaslog.memo.queries.QueriesMemoFacade
import org.springframework.stereotype.Controller

@Controller
class MemoAclInboundAdapter(
    private val memoQueries: QueriesMemoFacade,
) {
    fun isExistMemo(id: Long): Boolean {
        return memoQueries.handle(
            ICheckMemoExisted.Query(MemoId(id)),
        )
    }
}

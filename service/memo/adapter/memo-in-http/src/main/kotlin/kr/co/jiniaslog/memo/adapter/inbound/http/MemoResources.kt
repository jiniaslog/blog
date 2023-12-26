package kr.co.jiniaslog.memo.adapter.inbound.http

import kr.co.jiniaslog.memo.domain.folder.FolderId
import kr.co.jiniaslog.memo.domain.memo.AuthorId
import kr.co.jiniaslog.memo.domain.memo.MemoId
import kr.co.jiniaslog.memo.queries.IGetAllMemos
import kr.co.jiniaslog.memo.queries.IGetAllReferencedByMemo
import kr.co.jiniaslog.memo.queries.IGetAllReferencesByMemo
import kr.co.jiniaslog.memo.queries.IGetMemoById
import kr.co.jiniaslog.memo.queries.IRecommendRelatedMemo
import kr.co.jiniaslog.memo.queries.QueriesMemoFacade
import kr.co.jiniaslog.memo.usecase.IDeleteMemo
import kr.co.jiniaslog.memo.usecase.IInitMemo
import kr.co.jiniaslog.memo.usecase.IMakeRelationShipFolderAndMemo
import kr.co.jiniaslog.memo.usecase.IUpdateMemo
import kr.co.jiniaslog.memo.usecase.UseCasesMemoFacade
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URI

private val log = mu.KotlinLogging.logger { }

@RestController
@RequestMapping("/api/v1/memos")
class MemoResources(
    private val memoUseCases: UseCasesMemoFacade,
    private val memoQueries: QueriesMemoFacade,
) {
    @PostMapping
    @CrossOrigin(origins = ["http://localhost:3000"])
    fun initMemo(
        @RequestBody request: InitMemoRequest,
    ): ResponseEntity<InitMemoResponse> {
        val info =
            memoUseCases.handle(
                IInitMemo.Command(authorId = AuthorId(request.authorId)),
            )
        return ResponseEntity
            .created(URI("/api/v1/memos/${info.id.value}"))
            .body(InitMemoResponse(info.id.value))
    }

    @GetMapping
    @CrossOrigin(origins = ["http://localhost:3000"])
    fun getMemos(
        @RequestParam keyword: String?,
        @RequestParam thisId: Long?,
    ): MemoResponse {
        if (keyword.isNullOrBlank() && thisId == null) {
            return memoQueries.handle(IGetAllMemos.Query()).sortedBy { it.memoId.value }
                .toResponse()
        }

        return memoQueries.handle(IRecommendRelatedMemo.Query(keyword!!, MemoId(thisId!!)))
            .toResponse()
    }

    @GetMapping("/{id}")
    @CrossOrigin(origins = ["http://localhost:3000"])
    fun getMemoById(
        @PathVariable id: Long,
    ): GetMemoByIdResponse {
        return memoQueries.handle(IGetMemoById.Query(MemoId(id)))
            .toResponse()
    }

    @DeleteMapping("/{id}")
    @CrossOrigin(origins = ["http://localhost:3000"])
    fun deleteMemoById(
        @PathVariable id: Long,
    ): ResponseEntity<Unit> {
        memoUseCases.handle(IDeleteMemo.Command(MemoId(id)))
        return ResponseEntity
            .status(204)
            .build()
    }

    @PutMapping("/{id}/folders/{folderId}")
    @CrossOrigin(origins = ["http://localhost:3000"])
    fun addParentFolder(
        @PathVariable id: Long,
        @PathVariable folderId: Long,
    ): AddParentFolderResponse {
        return memoUseCases.handle(
            IMakeRelationShipFolderAndMemo.Command(
                memoId = MemoId(id),
                folderId = folderId.takeIf { it != -1L }?.let { FolderId(it) },
            ),
        ).toResponse()
    }

    @GetMapping("/{id}/references")
    @CrossOrigin(origins = ["http://localhost:3000"])
    fun getAllReferencesByMemo(
        @PathVariable id: Long,
    ): List<GetAllReferencesByMemoResponse> {
        val reference = memoQueries.handle(IGetAllReferencesByMemo.Query(MemoId(id)))
        return reference.map { it.toResponse() }
    }

    @GetMapping("/{id}/referenced")
    @CrossOrigin(origins = ["http://localhost:3000"])
    fun getAllReferencedByMemo(
        @PathVariable id: Long,
    ): List<GetAllReferencedByMemoResponse> {
        val referenced = memoQueries.handle(IGetAllReferencedByMemo.Query(MemoId(id)))
        return referenced.map { it.toResponse() }
    }

    @PutMapping("/{id}/references/{referenceId}")
    @CrossOrigin(origins = ["http://localhost:3000"])
    fun addReference(
        @PathVariable id: Long,
        @PathVariable referenceId: Long,
    ): AddReferenceResponse {
        return memoUseCases.handle(
            IUpdateMemo.Command.AddReference(
                memoId = MemoId(id),
                referenceId = MemoId(referenceId),
            ),
        ).toResponse()
    }

    @DeleteMapping("/{id}/references/{referenceId}")
    @CrossOrigin(origins = ["http://localhost:3000"])
    fun deleteReference(
        @PathVariable id: Long,
        @PathVariable referenceId: Long,
    ): ResponseEntity<Unit> {
        memoUseCases.handle(
            IUpdateMemo.Command.RemoveReference(
                memoId = MemoId(id),
                referenceId = MemoId(referenceId),
            ),
        )
        return ResponseEntity
            .status(204)
            .build()
    }
}

private fun IMakeRelationShipFolderAndMemo.Info.toResponse(): AddParentFolderResponse {
    return AddParentFolderResponse(
        memoId = this.memoId.value,
        folderId = this.folderId?.value,
    )
}

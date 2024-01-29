package kr.co.jiniaslog.memo.adapter.inbound.http

import kr.co.jiniaslog.memo.domain.folder.FolderId
import kr.co.jiniaslog.memo.domain.memo.AuthorId
import kr.co.jiniaslog.memo.queries.IGetFoldersAll
import kr.co.jiniaslog.memo.queries.QueriesFolderFacade
import kr.co.jiniaslog.memo.usecase.ICreateNewFolder
import kr.co.jiniaslog.memo.usecase.IDeleteFoldersRecursively
import kr.co.jiniaslog.memo.usecase.IMakeRelationShipFolderAndFolder
import kr.co.jiniaslog.memo.usecase.UseCasesFolderFacade
import org.springframework.security.access.prepost.PreAuthorize
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

private val log = mu.KotlinLogging.logger {}

@RestController
@RequestMapping("/api/v1/folders")
@PreAuthorize("hasRole('ADMIN')")
class FolderResources(
    private val folderUseCases: UseCasesFolderFacade,
    private val folderQueries: QueriesFolderFacade,
) {
    @PostMapping()
    @CrossOrigin(origins = ["http://localhost:3000"])
    fun initFolder(
        @AuthUserId userId: Long?,
    ): InitFolderResponse {
        val info =
            folderUseCases.handle(ICreateNewFolder.Command(AuthorId(userId!!)))
        return InitFolderResponse(info.id.value, info.folderName.value)
    }

    @PutMapping("/{folderId}/name")
    @CrossOrigin(origins = ["http://localhost:3000"])
    fun changeFolderName(
        @RequestBody request: ChangeFolderNameRequest,
    ): ChangeFolderNameResponse {
        val info =
            folderUseCases.handle(request.toCommand())
        return ChangeFolderNameResponse(info.folderId.value)
    }

    @PutMapping("/{folderId}/parent/{parentFolderId}")
    @CrossOrigin(origins = ["http://localhost:3000"])
    fun makeRelationshipWithFolders(
        @PathVariable folderId: Long,
        @PathVariable parentFolderId: Long?,
    ): MakeFolderRelationshipResponse {
        val info =
            folderUseCases.handle(
                IMakeRelationShipFolderAndFolder.Command(
                    parentFolderId?.let { FolderId(parentFolderId) },
                    FolderId(folderId),
                ),
            )
        return MakeFolderRelationshipResponse(info.parentFolderId?.value, info.childFolderId.value)
    }

    @DeleteMapping("/{folderId}")
    @CrossOrigin(origins = ["http://localhost:3000"])
    fun deleteFolder(
        @PathVariable folderId: Long,
    ): DeleteFolderResponse {
        val info =
            folderUseCases.handle(IDeleteFoldersRecursively.Command(FolderId(folderId)))
        return DeleteFolderResponse(info.folderId.value)
    }

    @GetMapping()
    @CrossOrigin(origins = ["http://localhost:3000"])
    fun getFoldersAll(
        @RequestParam(required = false) query: String?,
    ): FolderAndMemoResponse {
        return folderQueries.handle(IGetFoldersAll.Query(query)).toResponse()
    }
}

data class FolderAndMemoResponse(
    val folderInfos: List<IGetFoldersAll.FolderInfo>,
)

fun IGetFoldersAll.Info.toResponse(): FolderAndMemoResponse {
    return FolderAndMemoResponse(
        folderInfos = this.folderInfos,
    )
}

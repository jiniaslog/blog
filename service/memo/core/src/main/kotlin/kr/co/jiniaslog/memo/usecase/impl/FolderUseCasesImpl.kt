package kr.co.jiniaslog.memo.usecase.impl

import kr.co.jiniaslog.memo.domain.folder.Folder
import kr.co.jiniaslog.memo.domain.folder.FolderId
import kr.co.jiniaslog.memo.domain.folder.FolderRepository
import kr.co.jiniaslog.memo.usecase.IChangeFolderName
import kr.co.jiniaslog.memo.usecase.ICreateNewFolder
import kr.co.jiniaslog.memo.usecase.IDeleteFoldersRecursively
import kr.co.jiniaslog.memo.usecase.IMakeRelationShipFolderAndFolder
import kr.co.jiniaslog.shared.core.annotation.UseCaseInteractor

interface FolderUseCasesFacade :
    ICreateNewFolder,
    IChangeFolderName,
    IMakeRelationShipFolderAndFolder,
    IDeleteFoldersRecursively

@UseCaseInteractor
internal class FolderUseCases(
    private val folderRepository: FolderRepository,
) : FolderUseCasesFacade {
    override fun handle(command: ICreateNewFolder.Command): ICreateNewFolder.Info {
        val newOne =
            Folder.init(
                authorId = command.authorId,
                parent = command.parentFolderId,
            )
        folderRepository.save(newOne)
        return ICreateNewFolder.Info(newOne.id)
    }

    override fun handle(command: IChangeFolderName.Command): IChangeFolderName.Info {
        val folder = getFolder(command.folderId)
        folder.changeName(command.name)
        folderRepository.save(folder)
        return IChangeFolderName.Info(folder.id)
    }

    override fun handle(command: IMakeRelationShipFolderAndFolder.Command): IMakeRelationShipFolderAndFolder.Info {
        val parent = command.parentFolderId?.let { getFolder(it) }
        val child = getFolder(command.childFolderId)
        child.changeParent(parent)
        folderRepository.save(child)
        return IMakeRelationShipFolderAndFolder.Info(parent?.id, child.id)
    }

    override fun handle(command: IDeleteFoldersRecursively.Command): IDeleteFoldersRecursively.Info {
        val folder = getFolder(command.folderId)
        folderRepository.deleteById(folder.id)
        return IDeleteFoldersRecursively.Info(folder.id)
    }

    private fun getFolder(id: FolderId) =
        (
            folderRepository.findById(id)
                ?: throw IllegalArgumentException("FolderId : $id, folder not found")
        )
}
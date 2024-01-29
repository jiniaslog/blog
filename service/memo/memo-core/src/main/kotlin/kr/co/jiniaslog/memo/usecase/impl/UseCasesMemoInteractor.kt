package kr.co.jiniaslog.memo.usecase.impl

import kr.co.jiniaslog.memo.domain.folder.FolderId
import kr.co.jiniaslog.memo.domain.memo.Memo
import kr.co.jiniaslog.memo.domain.memo.MemoId
import kr.co.jiniaslog.memo.outbound.FolderRepository
import kr.co.jiniaslog.memo.outbound.MemoRepository
import kr.co.jiniaslog.memo.usecase.IDeleteMemo
import kr.co.jiniaslog.memo.usecase.IInitMemo
import kr.co.jiniaslog.memo.usecase.IMakeRelationShipFolderAndMemo
import kr.co.jiniaslog.memo.usecase.IUpdateMemo
import kr.co.jiniaslog.memo.usecase.UseCasesMemoFacade
import kr.co.jiniaslog.shared.core.annotation.UseCaseInteractor

@UseCaseInteractor
internal class UseCasesMemoInteractor(
    private val memoRepository: MemoRepository,
    private val folderRepository: FolderRepository,
) : UseCasesMemoFacade {
    override fun handle(command: IInitMemo.Command): IInitMemo.Info {
        val newOne =
            Memo.init(
                authorId = command.authorId,
                parentFolderId = command.parentFolderId,
            )
        memoRepository.save(newOne)
        return IInitMemo.Info(newOne.id)
    }

    override fun handle(command: IUpdateMemo.Command): IUpdateMemo.Info {
        val memo = getMemo(command.memoId)

        when (command) {
            is IUpdateMemo.Command.AddReference -> {
                validateMemoExistence(command.referenceId)
                memo.addReference(command.referenceId)
            }

            is IUpdateMemo.Command.RemoveReference -> {
                validateMemoExistence(command.referenceId)
                memo.removeReference(command.referenceId)
            }

            is IUpdateMemo.Command.UpdateForm -> {
                memo.update(command.title, command.content)
            }

            is IUpdateMemo.Command.UpdateReferences -> {
                command.references.forEach { validateMemoExistence(it) }
                memo.updateReferences(command.references)
            }
        }

        memoRepository.save(memo)
        return IUpdateMemo.Info(memo.id)
    }

    private fun validateMemoExistence(id: MemoId) {
        getMemo(id)
    }

    override fun handle(command: IDeleteMemo.Command): IDeleteMemo.Info {
        val memo = getMemo(command.id)

        memoRepository.deleteById(memo.id)
        return IDeleteMemo.Info()
    }

    override fun handle(command: IMakeRelationShipFolderAndMemo.Command): IMakeRelationShipFolderAndMemo.Info {
        val folder = command.folderId?.let { getFolder(it) }
        val memo = getMemo(command.memoId)

        memo.setParentFolder(folder?.id)

        memoRepository.save(memo)
        return IMakeRelationShipFolderAndMemo.Info(memo.id, command.folderId)
    }

    private fun getFolder(id: FolderId) =
        folderRepository.findById(id)
            ?: throw IllegalArgumentException("FolderId : $id, folder not found")

    private fun getMemo(id: MemoId) =
        memoRepository.findById(id)
            ?: throw IllegalArgumentException("MemoId : $id, memo not found")
}

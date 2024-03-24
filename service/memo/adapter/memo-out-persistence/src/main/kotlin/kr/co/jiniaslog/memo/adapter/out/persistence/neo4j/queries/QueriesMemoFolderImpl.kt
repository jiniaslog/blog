package kr.co.jiniaslog.memo.adapter.out.persistence.neo4j.queries

import kr.co.jiniaslog.memo.adapter.out.persistence.neo4j.folder.FolderNeo4jEntity
import kr.co.jiniaslog.memo.adapter.out.persistence.neo4j.folder.FolderNeo4jRepository
import kr.co.jiniaslog.memo.adapter.out.persistence.neo4j.memo.MemoNeo4jEntity
import kr.co.jiniaslog.memo.adapter.out.persistence.neo4j.memo.MemoNeo4jRepository
import kr.co.jiniaslog.memo.domain.folder.FolderId
import kr.co.jiniaslog.memo.domain.folder.FolderName
import kr.co.jiniaslog.memo.domain.memo.MemoContent
import kr.co.jiniaslog.memo.domain.memo.MemoId
import kr.co.jiniaslog.memo.domain.memo.MemoTitle
import kr.co.jiniaslog.memo.queries.ICheckMemoExisted
import kr.co.jiniaslog.memo.queries.IGetAllMemos
import kr.co.jiniaslog.memo.queries.IGetAllReferencedByMemo
import kr.co.jiniaslog.memo.queries.IGetAllReferencesByMemo
import kr.co.jiniaslog.memo.queries.IGetFoldersAll
import kr.co.jiniaslog.memo.queries.IGetMemoById
import kr.co.jiniaslog.memo.queries.IRecommendRelatedMemo
import kr.co.jiniaslog.memo.queries.QueriesFolderFacade
import kr.co.jiniaslog.memo.queries.QueriesMemoFacade
import kr.co.jiniaslog.shared.core.annotation.PersistenceAdapter
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@PersistenceAdapter
@Transactional(readOnly = true)
internal open class QueriesMemoFolderImpl(
    private val memoNeo4jRepository: MemoNeo4jRepository,
    private val folderNeo4jRepository: FolderNeo4jRepository,
) : QueriesMemoFacade, QueriesFolderFacade {
    override fun handle(query: IGetAllMemos.Query): List<IGetAllMemos.Info> {
        return memoNeo4jRepository.findAll().map {
            val id = it.id
            IGetAllMemos.Info(
                memoId = MemoId(it.id),
                title = MemoTitle(it.title),
                references =
                    it.references.map { IGetAllMemos.MemoReferenceInfo(rootId = MemoId(id), referenceId = MemoId(it.id)) }
                        .toSet(),
            )
        }
    }

    override fun handle(query: IRecommendRelatedMemo.Query): IRecommendRelatedMemo.Info {
        val relatedMemoCandidates =
            memoNeo4jRepository.findByKeywordFullTextSearchingLimit6(query.keyword)
                .filterNot { it.id == query.thisMemoId.value }
                .take(5)
                .map { Triple(it.id, it.title, it.content) }

        return IRecommendRelatedMemo.Info(
            relatedMemoCandidates =
                relatedMemoCandidates.map {
                    Triple(
                        MemoId(it.first),
                        MemoTitle(it.second),
                        MemoContent(it.third),
                    )
                },
        )
    }

    override fun handle(query: IGetMemoById.Query): IGetMemoById.Info {
        val memo =
            memoNeo4jRepository.findById(query.memoId.value).getOrNull()
                ?: throw IllegalArgumentException("memo not found")
        return IGetMemoById.Info(
            memoId = MemoId(memo.id),
            title = MemoTitle(memo.title),
            content = MemoContent(memo.content),
            references =
                memo.references.map {
                    IGetMemoById.Info.ReferenceInfo(
                        rootId = MemoId(it.id),
                        referenceId = MemoId(it.id),
                    )
                }.toSet(),
        )
    }

    // todo : n+1인지 쿼리 체크
    override fun handle(query: IGetAllReferencesByMemo.Query): IGetAllReferencesByMemo.Info {
        val memo =
            memoNeo4jRepository.findById(query.memoId.value).getOrNull()
                ?: throw IllegalArgumentException("memo not found")
        return IGetAllReferencesByMemo.Info(
            references =
                memo.references.map {
                    IGetAllReferencesByMemo.ReferenceInfo(
                        id = MemoId(it.id),
                        title = MemoTitle(it.title),
                    )
                }.toSet(),
        )
    }

    override fun handle(query: IGetAllReferencedByMemo.Query): IGetAllReferencedByMemo.Info {
        val result = memoNeo4jRepository.findReferencingMemos(query.memoId.value)
        return IGetAllReferencedByMemo.Info(
            referenceds =
                result.map {
                    IGetAllReferencedByMemo.ReferencedInfo(
                        id = MemoId(it.id),
                        title = MemoTitle(it.title),
                    )
                }.toSet(),
        )
    }

    override fun handle(query: ICheckMemoExisted.Query): Boolean {
        return memoNeo4jRepository.existsById(query.memoId.value)
    }

    override fun handle(query: IGetFoldersAll.Query): IGetFoldersAll.Info {
        if (query.value.isNullOrBlank()) {
            return findAll()
        }
        return findByKeyword(query)
    }

    private fun findAll(): IGetFoldersAll.Info {
        val foldersWithDepth = folderNeo4jRepository.findAll()
        val folderMap = foldersWithDepth.associate { it.id to it.toFolderInfo() }
        val memos = memoNeo4jRepository.findAll().groupBy { it.parentFolder?.id }

        foldersWithDepth.forEach { folder ->
            val folderInfo = folderMap[folder.id]
            folderInfo?.children = folderMap.values.filter { it.parent?.id == folderInfo?.id }
            folderInfo?.memos = memos[folderInfo?.id?.value]?.map { it.toMemoInfo() } ?: emptyList()
        }

        return IGetFoldersAll.Info(
            folderMap.values.filter { it.parent == null } +
                IGetFoldersAll.FolderInfo(
                    id = null,
                    name = FolderName("Uncategorized"),
                    parent = null,
                    children = emptyList(),
                    memos = memos[null]?.map { it.toMemoInfo() } ?: emptyList(),
                ),
        )
    }

    private fun findByKeyword(query: IGetFoldersAll.Query): IGetFoldersAll.Info {
        val memos = memoNeo4jRepository.findByKeywordFullTextSearching(query.value!!)
        return IGetFoldersAll.Info(
            listOf(
                IGetFoldersAll.FolderInfo(
                    id = null,
                    name = FolderName("검색 결과"),
                    parent = null,
                    children = emptyList(),
                    memos = memos.map { it.toMemoInfo() },
                ),
            ),
        )
    }

    private fun FolderNeo4jEntity.toFolderInfo(): IGetFoldersAll.FolderInfo {
        return IGetFoldersAll.FolderInfo(
            id = FolderId(this.id),
            name = FolderName(this.name),
            parent = this.parent?.toFolderInfo(),
            children = listOf(),
            memos = listOf(),
        )
    }

    private fun MemoNeo4jEntity.toMemoInfo(): IGetFoldersAll.MemoInfo {
        return IGetFoldersAll.MemoInfo(
            id = MemoId(this.id),
            title = MemoTitle(this.title),
            references = this.references.map { IGetFoldersAll.MemoReferenceInfo(MemoId(it.id), MemoTitle(it.title)) },
        )
    }
}

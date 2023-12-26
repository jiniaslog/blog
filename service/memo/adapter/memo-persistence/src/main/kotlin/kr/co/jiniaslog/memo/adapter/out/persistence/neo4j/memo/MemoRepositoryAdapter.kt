package kr.co.jiniaslog.memo.adapter.out.persistence.neo4j.memo

import kr.co.jiniaslog.memo.adapter.out.persistence.neo4j.folder.FolderNeo4jRepository
import kr.co.jiniaslog.memo.domain.memo.Memo
import kr.co.jiniaslog.memo.domain.memo.MemoId
import kr.co.jiniaslog.memo.domain.memo.MemoRepository
import kr.co.jiniaslog.shared.core.annotation.PersistenceAdapter
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@PersistenceAdapter
internal open class MemoRepositoryAdapter(
    private val memoNeo4jRepository: MemoNeo4jRepository,
    private val folderNeo4jRepository: FolderNeo4jRepository,
) : MemoRepository {
    @Transactional(readOnly = true)
    override fun findByRelatedMemo(keyword: String): List<Memo> {
        return memoNeo4jRepository.findByKeywordFullTextSearchingLimit6(keyword).map { it.toDomain() }
    }

    override fun findById(id: MemoId): Memo? {
        return memoNeo4jRepository.findById(id.value).orElse(null)?.toDomain()
    }

    @Transactional(readOnly = true)
    override fun findAll(): List<Memo> {
        return memoNeo4jRepository.findAll().map { it.toDomain() }
    }

    @Transactional
    override fun deleteById(id: MemoId) {
        memoNeo4jRepository.deleteById(id.value)
    }

    @Transactional
    override fun save(entity: Memo): Memo {
        val pm = memoNeo4jRepository.findById(entity.id.value).getOrNull()

        val memoNeo4jEntity =
            if (pm == null) {
                val reference =
                    entity.references.map {
                        memoNeo4jRepository.findById(it.referenceId.value).orElse(null)
                    }.toSet()

                memoNeo4jRepository.deleteParentFolderById(entity.id.value)
                val parentFolder =
                    entity.parentFolderId?.let {
                        folderNeo4jRepository.findById(it.value).orElse(null)
                    }

                MemoNeo4jEntity(
                    id = entity.id.value,
                    authorId = entity.authorId.value,
                    title = entity.title.value,
                    content = entity.content.value,
                    state = entity.state,
                    references = reference.toMutableSet(),
                    parentFolder = parentFolder,
                    createdAt = entity.createdAt,
                    updatedAt = entity.updatedAt,
                )
            } else {
                val origin: MemoNeo4jEntity = pm
                val updateData: Memo = entity

                // title
                if (origin.title != updateData.title.value) {
                    origin.title = updateData.title.value
                }

                // content
                if (origin.content != updateData.content.value) {
                    origin.content = updateData.content.value
                }

                // state
                if (origin.state != updateData.state) {
                    origin.state = updateData.state
                }

                // references
                if (origin.references != updateData.references.map { it.referenceId.value }.toSet()) {
                    origin.references =
                        updateData.references.map {
                            memoNeo4jRepository.findById(it.referenceId.value).orElse(null)
                        }.toMutableSet()
                }

                // parentFolder
                if (origin.parentFolder?.id != updateData.parentFolderId?.value) {
                    origin.parentFolder =
                        updateData.parentFolderId?.let {
                            folderNeo4jRepository.findById(it.value).orElse(null)
                        }
                }

                origin
            }

        return memoNeo4jRepository.save(memoNeo4jEntity).toDomain()
    }
}

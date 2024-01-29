package kr.co.jiniaslog.memo.adapter.out.persistence.neo4j.folder

import kr.co.jiniaslog.memo.adapter.out.persistence.neo4j.memo.MemoNeo4jRepository
import kr.co.jiniaslog.memo.domain.folder.Folder
import kr.co.jiniaslog.memo.domain.folder.FolderId
import kr.co.jiniaslog.memo.outbound.FolderRepository
import kr.co.jiniaslog.shared.core.annotation.PersistenceAdapter
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@PersistenceAdapter
internal open class FolderRepositoryAdapter(
    private val folderNeo4jRepository: FolderNeo4jRepository,
    private val memoNeo4jRepository: MemoNeo4jRepository,
) : FolderRepository {
    @Transactional(readOnly = true)
    override fun findById(id: FolderId): Folder? {
        return folderNeo4jRepository.findById(id.value).orElse(null)?.toDomain()
    }

    @Transactional(readOnly = true)
    override fun findAll(): List<Folder> {
        return folderNeo4jRepository.findAll().map { it.toDomain() }
    }

    @Transactional
    override fun deleteById(id: FolderId) {
        folderNeo4jRepository.deleteFolderRecursivelyById(id.value)
    }

    @Transactional
    override fun save(entity: Folder): Folder {
        var pm = folderNeo4jRepository.findById(entity.id.value).getOrNull()

        pm =
            when (pm) {
                null -> { // is new
                    val parentFolder =
                        entity.parent?.let {
                            folderNeo4jRepository.findById(it.value).getOrNull()
                        }

                    val folderNeo4jEntity =
                        FolderNeo4jEntity(
                            id = entity.id.value,
                            name = entity.name.value,
                            authorId = entity.authorId.value,
                            parent = parentFolder,
                            createdAt = entity.createdAt,
                            updatedAt = entity.updatedAt,
                        )
                    folderNeo4jRepository.save(folderNeo4jEntity)
                }

                else -> { // update
                    val origin: FolderNeo4jEntity = pm
                    val updateData: Folder = entity

                    // name
                    if (origin.name != updateData.name.value) {
                        origin.name = updateData.name.value
                    }

                    // parent
                    val updatedParentId = updateData.parent?.value
                    if ((origin.parent?.id) != updatedParentId) {
                        folderNeo4jRepository.deleteRelationshipContainsById(origin.id)
                        origin.parent =
                            updatedParentId?.let {
                                folderNeo4jRepository.findById(it).getOrNull()
                            }
                    }
                    folderNeo4jRepository.save(origin)
                }
            }
        return pm.toDomain()
    }
}

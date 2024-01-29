package kr.co.jiniaslog.memo.adapter.out.persistence.neo4j.memo

import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.neo4j.repository.query.Query
import org.springframework.data.repository.query.Param

internal interface MemoNeo4jRepository : Neo4jRepository<MemoNeo4jEntity, Long> {
    @Query(
        """
        CALL db.index.fulltext.queryNodes("memo_full_text_index", ${'$'}keyword)
        YIELD node, score
        OPTIONAL MATCH (node)-[:REFERENCE]->(other)
        WITH node, score, count(other) AS referencesCount
        ORDER BY referencesCount DESC, score DESC
       RETURN 
            node.id AS id, 
            node.authorId AS authorId, 
            node.title AS title, 
            node.content AS content, 
            node.state AS state, 
            node.createdAt AS createdAt, 
            node.updatedAt AS updatedAt
        LIMIT 6
    """,
    )
    fun findByKeywordFullTextSearchingLimit6(
        @Param("keyword") keyword: String,
    ): List<MemoNeo4jEntity>

    @Query(
        """
        CALL db.index.fulltext.queryNodes("memo_full_text_index", ${'$'}keyword)
        YIELD node, score
        OPTIONAL MATCH (node)-[:REFERENCE]->(other)
        WITH node, score, count(other) AS referencesCount
        ORDER BY referencesCount DESC, score DESC
       RETURN 
            node.id AS id, 
            node.authorId AS authorId, 
            node.title AS title, 
            node.content AS content, 
            node.state AS state, 
            node.createdAt AS createdAt, 
            node.updatedAt AS updatedAt
    """,
    )
    fun findByKeywordFullTextSearching(
        @Param("keyword") keyword: String,
    ): List<MemoNeo4jEntity>

    @Query("MATCH (parent:memo)-[r:REFERENCE]->(child:memo) WHERE child.id = ${'$'}memoId detach DELETE r")
    fun deleteParentFolderById(
        @Param("memoId") memoId: Long,
    )

    @Query("MATCH (referencingMemo:memo)-[r:REFERENCE]->(m:memo) WHERE m.id = ${'$'}memoId RETURN referencingMemo")
    fun findReferencingMemos(memoId: Long): List<MemoNeo4jEntity>
}

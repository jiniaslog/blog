package kr.co.jiniaslog.memo

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.neo4j.core.Neo4jClient
import org.springframework.stereotype.Component

@Component
class Neo4jDbCleaner {
    @Autowired
    private lateinit var neo4jClient: Neo4jClient

    fun tearDown() {
        neo4jClient.query("MATCH (n) DETACH DELETE n").run()
    }
}

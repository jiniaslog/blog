package kr.co.jiniaslog.blogcore.adapter.persistence

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.DataSourceUtils
import org.springframework.stereotype.Component
import java.sql.Connection
import javax.sql.DataSource

/**
 * Db cleaner
 *
 * 각 배치 통합 테스트 코드 실행 후 모든 DB table의 row를 날리는 클리너
 *
 */
@Component
class DbCleaner(private val dataSources: List<DataSource>) {

    fun cleanAll() {
        dataSources.forEach { dataSource ->
            clean(dataSource)
        }
    }

    private fun clean(dataSource: DataSource) {
        val jdbcTemplate = JdbcTemplate(dataSource)
        var connection: Connection? = null
        try {
            connection = DataSourceUtils.getConnection(jdbcTemplate.dataSource!!)
            val schema: String = connection.catalog

            val tableNames: List<String> = jdbcTemplate.query(
                "SELECT table_name FROM information_schema.tables WHERE table_schema = ?",
                arrayOf(schema)
            ) { resultSet, _ ->
                resultSet.getString("table_name")
            }

            tableNames.forEach { tableName ->
                jdbcTemplate.execute("TRUNCATE TABLE $tableName")
            }
        } finally {
            if (connection != null) {
                DataSourceUtils.releaseConnection(connection, jdbcTemplate.dataSource!!)
            }
        }
    }
}

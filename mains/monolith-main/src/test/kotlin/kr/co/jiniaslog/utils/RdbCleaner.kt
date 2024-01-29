package kr.co.jiniaslog.utils

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.DataSourceUtils
import org.springframework.stereotype.Component
import java.sql.Connection
import javax.sql.DataSource

@Component
class RdbCleaner(private val dataSources: List<DataSource>) {
    fun tearDownAll() {
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

            val tableNames: List<String> =
                jdbcTemplate.query(
                    "SELECT table_name FROM information_schema.tables WHERE table_schema = ?",
                    arrayOf(schema),
                ) { resultSet, _ ->
                    resultSet.getString("table_name")
                }
            jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS=0;")
            tableNames.forEach { tableName ->
                jdbcTemplate.execute("TRUNCATE TABLE $tableName")
            }
            jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS=1;")
        } finally {
            if (connection != null) {
                DataSourceUtils.releaseConnection(connection, jdbcTemplate.dataSource!!)
            }
        }
    }
}

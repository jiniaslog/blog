package kr.co.jiniaslog.shared.adapter.out.rdb

import com.p6spy.engine.logging.Category
import com.p6spy.engine.spy.appender.MessageFormattingStrategy
import org.hibernate.engine.jdbc.internal.FormatStyle
import org.springframework.stereotype.Component
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Component
internal class SqlFormatter : MessageFormattingStrategy {
    override fun formatMessage(
        connectionId: Int,
        now: String,
        elapsed: Long,
        category: String,
        prepared: String,
        sql: String,
        url: String,
    ): String {
        var targetSql: String? = sql
        targetSql = formatSql(category, targetSql)
        val currentDate = Date()
        val format1 = SimpleDateFormat("yy.MM.dd HH:mm:ss")

//        return now + "|" + elapsed + "ms|" + category + "|connection " + connectionId + "|" + P6Util.singleLine(prepared) + sql;
        return format1.format(currentDate) + " | " + "OperationTime : " + elapsed + "ms" + targetSql
    }

    fun formatSql(
        category: String,
        sql: String?,
    ): String? {
        var targetSql = sql
        if (targetSql == null || targetSql!!.trim { it <= ' ' } == "") return targetSql

        // Only format Statement, distinguish DDL And DML
        if (Category.STATEMENT.name.equals(category)) {
            val tmpSql = targetSql!!.trim { it <= ' ' }.lowercase(Locale.ROOT)
            targetSql =
                if (tmpSql.startsWith("create") || tmpSql.startsWith("alter") || tmpSql.startsWith("comment")) {
                    FormatStyle.DDL.formatter.format(targetSql)
                } else {
                    FormatStyle.BASIC.formatter.format(targetSql)
                }
            targetSql = "|\nHeFormatSql(P6Spy sql,Hibernate format):$targetSql"
        }
        return targetSql
    }
}

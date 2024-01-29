data class Module(val path: String)

object Modules {
    object Mains {
        val MonolithMain = Module(":mains:monolith-main")
    }

    object Service {
        object Memo {
            val Core = Module(":service:memo:memo-core")
            val ReadOnly = Module(":service:memo:memo-queries")

            enum class Adaptors(val path: String) {
                InHttp(":service:memo:adapter:memo-in-http"),
                InWebsocket(":service:memo:adapter:memo-in-websocket"),
                Persistence(":service:memo:adapter:memo-out-persistence")
                ;
            }
        }

        object Media {
            val Core = Module(":service:media:media-core")

            enum class Adaptors(val path: String) {
                InHttp(":service:media:adapter:media-in-http"),
                OutGithub(":service:media:adapter:media-out-github")
                ;
            }
        }

        object Blog {
            val Core = Module(":service:blog:blog-core")

            enum class Adaptors(val path: String) {
                InHttp(":service:blog:adapter:blog-in-http"),
                Persistence(":service:blog:adapter:blog-persistence")
                ;
            }
        }

        object AuthUser {
            val Core = Module(":service:user-auth:user-auth-core")
            val Application = Module(":service:user-auth:user-auth-application")

            enum class Adaptors(val path: String) {
                InHttp(":service:user-auth:adapter:user-auth-in-http"),
                OutGoogle(":service:user-auth:adapter:user-auth-out-google"),
                Persistence(":service:user-auth:adapter:user-auth-out-persistence"),
                Cache(":service:user-auth:adapter:user-auth-out-cache")
                ;
            }
        }

        val MessageNexus = Module(":service:message-nexus")
    }

    object Libs {
        val SnowflakeIdGenerator = Module(":libs:snowflake-id-generator")
        val GlobalLogging = Module(":libs:global-logging")
        val CoreKernel = Module(":libs:core-kernel")
        val JpaKernel = Module(":libs:rdb-kernel")
    }
}

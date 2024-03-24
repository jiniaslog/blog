rootProject.name = "Jinia's Log"

include(
    /**
     * ################
     * ## Jinia's Log##
     * ################
     */

    // framework 의존성을 가지고 순수하게 애플리케이션을 실행시키는 main module
    "mains:monolith-main",

    // 서비스 공용 라이브러리
    "libs:core-kernel",
    "libs:messaging-handler-generator",
    "libs:rdb-kernel",
    "libs:rest-kernel",
    "libs:messaging-kernel",
    "libs:global-logging",
    "libs:snowflake-id-generator",

    // blog
    "service:blog:blog-core",
    "service:blog:adapter:blog-out-rdb",
    "service:blog:adapter:blog-in-http",
    "service:blog:adapter:blog-in-message",
    "service:blog:adapter:blog-out-user",
    "service:blog:adapter:blog-out-memo",

    // memo
    "service:memo:memo-core",
    "service:memo:memo-queries",
    "service:memo:adapter:memo-in-http",
    "service:memo:adapter:memo-in-acl",
    "service:memo:adapter:memo-in-websocket",
    "service:memo:adapter:memo-in-message",
    "service:memo:adapter:memo-in-batch",
    "service:memo:adapter:memo-out-persistence",

    //media
    "service:media:media-core",
    "service:media:adapter:media-in-http",
    "service:media:adapter:media-out-github",

    //user-auth
    "service:user-auth:user-auth-core",
    "service:user-auth:user-auth-application",
    "service:user-auth:adapter:user-auth-in-http",
    "service:user-auth:adapter:user-auth-in-acl",
    "service:user-auth:adapter:user-auth-out-google",
    "service:user-auth:adapter:user-auth-out-persistence",
    "service:user-auth:adapter:user-auth-out-cache",

    "service:comment",
    "service:seo",
    "service:message-nexus",
)


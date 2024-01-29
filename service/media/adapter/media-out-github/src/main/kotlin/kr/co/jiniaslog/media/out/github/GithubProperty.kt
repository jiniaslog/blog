package kr.co.jiniaslog.media.out.github

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "api.github")
class GithubProperty
    @ConstructorBinding
    constructor(
        val gitToken: String,
        val gitRepo: String,
        val rootUrl: String,
    )

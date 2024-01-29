package kr.co.jiniaslog.media.out.github

import kr.co.jiniaslog.media.domain.ImageFile
import kr.co.jiniaslog.media.domain.ImageUrl
import kr.co.jiniaslog.media.outbound.ImageUploadStrategy
import org.kohsuke.github.GitHubBuilder
import org.springframework.stereotype.Component

@Component
class GithubImageUploadStrategy(
    private val githubProperty: GithubProperty,
) : ImageUploadStrategy {
    override fun uploadImage(imageFile: ImageFile): ImageUrl {
        val gitHub = GitHubBuilder().withOAuthToken(githubProperty.gitToken).build()
        val repository = gitHub.getRepository(githubProperty.gitRepo)
        repository.createContent().path("img/${imageFile.id.value}")
            .content(imageFile.rawImage.value).message("thumbnail").branch("main").commit()
        return ImageUrl("${githubProperty.rootUrl}${imageFile.id.value}?raw=true")
    }
}

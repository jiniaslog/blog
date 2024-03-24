package kr.co.jiniaslog.blog.adapter.inbound.http

import kr.co.jiniaslog.blog.domain.article.ArticleId
import kr.co.jiniaslog.blog.usecase.ArticleSimpleCommandsFacade
import kr.co.jiniaslog.blog.usecase.IDeleteArticle
import kr.cojiniaslog.shared.adapter.inbound.http.AuthUserId
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/api/v1/articles")
class ArticleResources(
    private val articleFacade: ArticleSimpleCommandsFacade,
) {
    @PostMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    fun postNewArticle(
        @AuthUserId userId: Long?,
        @RequestBody request: ArticlePostRequest,
    ): ResponseEntity<ArticlePostResponse> {
        require(userId != null) { "userId must not be null" }
        val info = articleFacade.handle(request.toCommand(userId))

        return ResponseEntity
            .created(URI("/api/v1/articles/${info.articleId.value}"))
            .body(ArticlePostResponse(info.articleId.value))
    }

    @PostMapping("/{articleId}")
    @PreAuthorize("hasRole('ADMIN')")
    fun updateArticle(
        @AuthUserId userId: Long?,
        @RequestBody request: ArticleUpdateRequest,
        @PathVariable articleId: Long,
    ): ResponseEntity<ArticleUpdateResponse> {
        require(userId != null) { "userId must not be null" }
        val info = articleFacade.handle(request.toCommand(articleId))

        return ResponseEntity
            .ok(ArticleUpdateResponse(info.id.value))
    }

    @DeleteMapping("/{articleId}")
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteArticle(
        @AuthUserId userId: Long?,
        @PathVariable articleId: Long,
    ): ResponseEntity<ArticleDeleteResponse> {
        require(userId != null) { "userId must not be null" }
        articleFacade.handle(IDeleteArticle.Command(ArticleId(articleId)))
        return ResponseEntity.ok(ArticleDeleteResponse(articleId))
    }
}

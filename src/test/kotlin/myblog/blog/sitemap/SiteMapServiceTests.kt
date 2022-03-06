package myblog.blog.sitemap

import com.nhaarman.mockito_kotlin.whenever
import myblog.blog.article.domain.Article
import myblog.blog.article.service.ArticleService
import myblog.blog.rss.RssService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
//import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.junit.jupiter.MockitoExtension
import java.lang.reflect.Field
import java.time.LocalDateTime
import java.util.*


@ExtendWith(MockitoExtension::class)
class SiteMapServiceTests {

    @Mock
    lateinit var articleService: ArticleService
    @InjectMocks
    lateinit var rssService: RssService

    @Test
    fun `rss 요청시 성공 테스트`() {
        // given
        whenever(articleService.totalArticle)
                .thenReturn(Arrays.asList(buildArticle("테스트용", "1호", 1L), buildArticle("테스트용이에용", "2호", 2L)))
        // when
        val rssFeed = rssService.rssFeed
        // then
        assertThat(rssFeed).contains("<title><![CDATA[테스트용]]></title>")
                .contains("<link>https://www.jiniaslog.co.kr/article/view?articleId=1</link>")
                .contains("<description><![CDATA[<p>1호</p>]]></description>")
                .contains("<guid>https://www.jiniaslog.co.kr/article/view?articleId=1</guid>")
        assertThat(rssFeed).contains("<title><![CDATA[테스트용이에용]]></title>")
                .contains("<link>https://www.jiniaslog.co.kr/article/view?articleId=2</link>")
                .contains("<description><![CDATA[<p>2호</p>]]></description>")
                .contains("<guid>https://www.jiniaslog.co.kr/article/view?articleId=2</guid>")
    }

    private fun buildArticle(title: String, content: String, id: Long): Article? {
        val article = Article.builder().title(title).content(content).build()
        setArticlePrivateFieldId(id, article)
        setArticleCreatedTimeStamp(article)
        return article
    }

    private fun setArticleCreatedTimeStamp(article: Article) {
        val clazz = Class.forName("myblog.blog.article.domain.Article").superclass
        val field: Field = clazz.getDeclaredField("createdDate")
        field.setAccessible(true)
        field.set(article, LocalDateTime.now())
    }

    private fun setArticlePrivateFieldId(id: Long, article: Article) {
        val clazz = Class.forName("myblog.blog.article.domain.Article")
        val field: Field = clazz.getDeclaredField("id")
        field.setAccessible(true)
        field.set(article, id)
    }
}
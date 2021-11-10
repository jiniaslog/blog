package myblog.blog.tags.domain;

import lombok.Builder;
import myblog.blog.article.domain.Article;
import myblog.blog.base.domain.BasicEntity;

import javax.persistence.*;

@Entity
@SequenceGenerator(
        name = "ARTICLE_TAG_LIST_SEQ_GENERATOR",
        sequenceName = "ARTICLE_TAG_LIST_SEQ",
        initialValue = 1, allocationSize = 50)
public class ArticleTagList extends BasicEntity {
    @Id
    @Column(name = "article_tag_list_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ARTICLE_TAG_LIST_SEQ_GENERATOR")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne
    @JoinColumn(name = "tags_id")
    private Tags tags;

    @Builder
    public ArticleTagList(Article article, Tags tags) {
        this.article = article;
        this.tags = tags;
    }

    protected ArticleTagList() {

    }
}

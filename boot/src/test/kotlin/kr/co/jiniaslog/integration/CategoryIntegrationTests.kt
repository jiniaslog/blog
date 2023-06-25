package kr.co.jiniaslog.integration

import io.restassured.RestAssured.given
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import kr.co.jiniaslog.blogcore.adapter.persistence.CoreDB
import kr.co.jiniaslog.blogcore.domain.category.Category
import kr.co.jiniaslog.blogcore.domain.category.CategoryId
import kr.co.jiniaslog.blogcore.domain.category.CategoryRepository
import kr.co.jiniaslog.config.TestContainerConfig
import org.apache.http.HttpStatus
import org.apache.http.protocol.HTTP
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class CategoryIntegrationTests : TestContainerConfig() {
    @Autowired
    private lateinit var categoryRepository: CategoryRepository

    @PersistenceContext(unitName = CoreDB.PERSISTENT_UNIT)
    private lateinit var em: EntityManager

    @Test
    fun `category 싱크 테스트`() {
        // given:
        categoryRepository.save(
            Category.newOne(
                id = CategoryId(1),
                label = "상위 카테고리 1",
                order = 1,
                parentId = null,
            )
        )

        categoryRepository.save(
            Category.newOne(
                id = CategoryId(2),
                label = "상위 카테고리 2",
                order = 2,
                parentId = null,
            )
        )

        categoryRepository.save(
            Category.newOne(
                id = CategoryId(3),
                label = "하위 기존 카테고리 1",
                order = 5,
                parentId = CategoryId(1),
            )
        )

        em.clear()

        val request = given()
            .header(HTTP.CONTENT_TYPE, "application/json")
            .body(
                """
{
    "categories": [
        {
        "id": 1,
        "label": "상위 카테고리 1",
        "order": 1,
        "parentLabel": null
        },
        {
        "id": 2,
        "label": "상위 카테고리 2",
        "order": 2,
        "parentLabel": null
        },
        {
        "id": null,
        "label": "하위 신규 카테고리 1",
        "order": 1,
        "parentLabel": "상위 카테고리 1"
        },
        {
        "id": null,
        "label": "하위 신규 카테고리 2",
        "order": 2,
        "parentLabel": "상위 카테고리 1"
        },
{
        "id": 3,
        "label": "하위 기존 카테고리 1",
        "order": 1,
        "parentLabel": "상위 카테고리 2"
        }
    ]
}
""".trimIndent()
            )

        // when:
        val response = given().spec(request)
            .`when`().put("/api/categories")
            .then().extract()

        // then:
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_OK)
        val findAll = categoryRepository.findAll()
        Assertions.assertThat(findAll.size).isEqualTo(5)
        val sortedByAllCategories = findAll.sortedBy { it.id.value }
        Assertions.assertThat(sortedByAllCategories[2].order).isEqualTo(1)
    }
}
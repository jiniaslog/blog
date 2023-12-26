package kr.co.jiniaslog.integration

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.restassured.RestAssured
import kr.co.jiniaslog.memo.domain.memo.MemoId
import kr.co.jiniaslog.memo.domain.memo.MemoRepository
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.neo4j.core.Neo4jClient

class MemoIntegrationTests : TestContainerAbstractSkeleton() {
    @Autowired
    lateinit var memoRepository: MemoRepository

    @Autowired
    lateinit var neo4jClient: Neo4jClient

    @Test
    fun `유효한 메모 생성 요청을 하면 메모가 생성되어야 한다`() {
        val result =
            RestAssured
                .given()
                .body(
                    """
                    {
                     "authorId": 1
                    }
                    """.trimIndent(),
                )
                .contentType("application/json")
                .`when`()
                .post("/api/v1/memos")
                .then()
                .log().all()
                // 출력 검증
                .statusCode(201)
                .body("memoId", notNullValue())
                .extract()

        // 상태 검증
        val memoId = result.path<Long>("memoId")
        memoRepository.findAll().size shouldBe 1
        val foundTarget = memoRepository.findById(MemoId(memoId))
        foundTarget shouldNotBe null
        foundTarget!!.authorId.value shouldBe 1
    }

    @Test
    fun `유효한 메모 삭제 요청이 있다면 해당 메모는 삭제되어야 한다`() {
        // background
        val result =
            RestAssured
                .given()
                .body(
                    """
                    {
                     "authorId": 1
                    }
                    """.trimIndent(),
                )
                .contentType("application/json")
                .`when`()
                .post("/api/v1/memos")
                .then()
                .extract()
        val memoId = result.path<Long>("memoId")

        RestAssured
            .given()
            .contentType("application/json")
            .`when`()
            .delete("/api/v1/memos/$memoId")
            .then()
            .log().all()
            .statusCode(204)

        // 상태 검증
        memoRepository.findById(MemoId(memoId)) shouldBe null
    }
}

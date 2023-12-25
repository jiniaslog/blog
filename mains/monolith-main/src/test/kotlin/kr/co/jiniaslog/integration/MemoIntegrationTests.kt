package kr.co.jiniaslog.integration

import io.restassured.RestAssured
import org.junit.jupiter.api.Test

class MemoIntegrationTests : TestContainerAbstractSkeleton() {
    @Test
    fun `유효한 메모 생성 요청을 하면 메모가 생성되어야 한다`() {
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
            .statusCode(201)
    }
}

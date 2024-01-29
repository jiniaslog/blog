package kr.co.jiniaslog.memo.domain.folder

import io.kotest.assertions.throwables.shouldThrow
import kr.co.jiniaslog.shared.CustomBehaviorSpec

class FolderNameTests : CustomBehaviorSpec() {
    init {
        given("빈문자열로") {
            `when`("FolderName을 생성하면") {
                then("실패한다") {
                    shouldThrow<IllegalArgumentException> {
                        FolderName("")
                    }
                }
            }
        }
        given("유효한 문자열로") {
            `when`("FolderName을 생성하면") {
                then("성공한다") {
                    FolderName("folder")
                }
            }
        }
    }
}

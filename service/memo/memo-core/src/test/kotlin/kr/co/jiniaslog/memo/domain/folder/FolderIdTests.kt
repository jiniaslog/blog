package kr.co.jiniaslog.memo.domain.folder

import io.kotest.assertions.throwables.shouldThrow
import kr.co.jiniaslog.shared.CustomBehaviorSpec

class FolderIdTests : CustomBehaviorSpec() {
    init {
        given("음수로") {
            `when`("FolderId를 생성하면") {
                then("실패한다") {
                    shouldThrow<IllegalArgumentException> {
                        FolderId(-1L)
                    }
                }
            }
        }
        given("0으로") {
            `when`("FolderId를 생성하면") {
                then("실패한다") {
                    shouldThrow<IllegalArgumentException> {
                        FolderId(0L)
                    }
                }
            }
        }
        given("양수로") {
            `when`("FolderId를 생성하면") {
                then("성공한다") {
                    FolderId(1L)
                }
            }
        }
    }
}

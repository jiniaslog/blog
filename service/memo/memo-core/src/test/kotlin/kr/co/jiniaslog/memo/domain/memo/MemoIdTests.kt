package kr.co.jiniaslog.memo.domain.memo

import io.kotest.assertions.throwables.shouldThrow
import kr.co.jiniaslog.shared.CustomBehaviorSpec
import kr.co.jiniaslog.shared.core.domain.vo.ValueObject

class MemoIdTests : CustomBehaviorSpec() {
    init {
        given("음수를 가지고") {
            `when`("MemoId를 생성하면") {
                then("실패한다") {
                    shouldThrow<IllegalArgumentException> {
                        MemoId(-1)
                    }
                }
            }
        }
        given("0을 가지고") {
            `when`("MemoId를 생성하면") {
                then("실패한다") {
                    shouldThrow<IllegalArgumentException> {
                        MemoId(0)
                    }
                }
            }
        }
        given("양수를 가지고") {
            `when`("MemoId를 생성하면") {
                then("성공한다") {
                    val id: ValueObject = MemoId(1)
                    id.validate()
                }
            }
        }
    }
}

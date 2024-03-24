package kr.co.jiniaslog.blog.usecase

import kr.co.jiniaslog.blog.domain.tag.TagId
import kr.co.jiniaslog.blog.domain.tag.TagName

/**
 * 태그의 기본적인 유즈케이스는 계속 생성되며, 일정 주기마다 전혀 사용되지 않는 태그를 제거하기로 한다.
 *
 * 기존 태그에 대한 업데이트는 존재하지 않는다.
 */
interface TagUseCasesFacade :
    ICreateNewTag

/**
 * 새로운 태그를 생성한다.
 */
interface ICreateNewTag {
    fun handle(command: Command): Info

    data class Command(
        val name: TagName,
    )

    data class Info(
        val id: TagId,
    )
}

/**
 * 사용되지 않는 태그를 제거한다.
 */
interface IDeleteNotUsedTags {
    fun handle(command: Command): Info

    class Command()

    data class Info(
        val deletedTagNames: List<TagName>,
    )
}

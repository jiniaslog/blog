package kr.co.jiniaslog.blog.usecase.impl

import kr.co.jiniaslog.blog.domain.tag.Tag
import kr.co.jiniaslog.blog.outbound.persistence.BlogTransactionHandler
import kr.co.jiniaslog.blog.outbound.persistence.TagRepository
import kr.co.jiniaslog.blog.usecase.ICreateNewTag
import kr.co.jiniaslog.blog.usecase.TagUseCasesFacade
import kr.co.jiniaslog.shared.core.annotation.UseCaseInteractor

@UseCaseInteractor
class TagUseCaseInteractor(
    private val tagRepository: TagRepository,
    private val transactionHandler: BlogTransactionHandler,
) : TagUseCasesFacade {
    override fun handle(command: ICreateNewTag.Command): ICreateNewTag.Info =
        with(command) {
            require(tagRepository.isExist(name).not()) { "tag already exists" }

            val newTag = Tag.newOne(name)
            transactionHandler.runInRepeatableReadTransaction {
                tagRepository.save(newTag)
            }

            ICreateNewTag.Info(newTag.id)
        }
}

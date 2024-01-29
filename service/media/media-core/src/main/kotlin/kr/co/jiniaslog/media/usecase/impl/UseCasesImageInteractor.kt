package kr.co.jiniaslog.media.usecase.impl

import kr.co.jiniaslog.media.domain.ImageFile
import kr.co.jiniaslog.media.domain.ImageId
import kr.co.jiniaslog.media.outbound.ImageUploadStrategy
import kr.co.jiniaslog.media.usecase.IUploadImage
import kr.co.jiniaslog.media.usecase.UseCasesImageFacade
import kr.co.jiniaslog.shared.core.annotation.UseCaseInteractor
import kr.co.jiniaslog.shared.core.domain.IdUtils

@UseCaseInteractor
class UseCasesImageInteractor(
    private val imageUploadStrategy: ImageUploadStrategy,
) : UseCasesImageFacade {
    override fun uploadImage(command: IUploadImage.Command): IUploadImage.Info {
        command.validate()
        val imageFile =
            ImageFile(
                id = ImageId(IdUtils.generate()),
                rawImage = command.rawImage,
                authorId = command.authorId,
            )
        val url = imageUploadStrategy.uploadImage(imageFile)
        return IUploadImage.Info(url = url)
    }

    private fun IUploadImage.Command.validate() {}
}

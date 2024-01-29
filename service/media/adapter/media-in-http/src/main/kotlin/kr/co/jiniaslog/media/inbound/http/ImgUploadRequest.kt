package kr.co.jiniaslog.media.inbound.http

import kr.co.jiniaslog.media.domain.AuthorId
import kr.co.jiniaslog.media.domain.RawImage
import kr.co.jiniaslog.media.usecase.IUploadImage
import org.springframework.web.multipart.MultipartFile

data class ImgUploadRequest(
    val image: MultipartFile,
) {
    fun toCommand() =
        IUploadImage.Command(
            rawImage = RawImage(image.bytes),
            authorId = AuthorId(1),
        )
}

data class ImgUploadResponse(
    val url: String,
)

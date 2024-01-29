package kr.co.jiniaslog.media.inbound.http

import kr.co.jiniaslog.media.usecase.UseCasesImageFacade
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/media")
class MediaController(
    private val useCaseImage: UseCasesImageFacade,
) {
    @PostMapping("/image")
    @CrossOrigin(origins = ["http://localhost:3000"])
    fun uploadImage(
        @ModelAttribute request: ImgUploadRequest,
    ): ImgUploadResponse {
        val info = useCaseImage.uploadImage(request.toCommand())
        return ImgUploadResponse(url = info.url.value)
    }
}

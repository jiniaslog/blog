package kr.co.jiniaslog.media.outbound

import kr.co.jiniaslog.media.domain.ImageFile
import kr.co.jiniaslog.media.domain.ImageUrl

interface ImageUploadStrategy {
    fun uploadImage(imageFile: ImageFile): ImageUrl
}

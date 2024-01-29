package kr.co.jiniaslog.media.domain

import kr.co.jiniaslog.shared.core.domain.AggregateRoot

class ImageFile(
    id: ImageId,
    rawImage: RawImage,
    authorId: AuthorId,
) : AggregateRoot<ImageId>() {
    override val id: ImageId = id

    val rawImage: RawImage = rawImage

    val authorId: AuthorId = authorId
}

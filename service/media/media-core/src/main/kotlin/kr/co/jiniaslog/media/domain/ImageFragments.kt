package kr.co.jiniaslog.media.domain

import kr.co.jiniaslog.shared.core.domain.vo.ValueObject

@JvmInline
value class RawImage(val value: ByteArray) : ValueObject {
    init {
        validate()
    }

    override fun validate() {
        require(value.isNotEmpty()) {
            "rawImage must not be empty"
        }
    }
}

@JvmInline
value class ImageId(val value: Long) : ValueObject {
    init {
        validate()
    }

    override fun validate() {
        require(value > 0) {
            "imageId must be positive"
        }
    }
}

@JvmInline
value class AuthorId(val value: Long) : ValueObject {
    init {
        validate()
    }

    override fun validate() {
        require(value > 0) {
            "authorId must be positive"
        }
    }
}

@JvmInline
value class ImageUrl(val value: String) : ValueObject {
    init {
        validate()
    }

    override fun validate() {
        require(value.isNotBlank()) {
            "imageUrl must not be blank"
        }
    }
}

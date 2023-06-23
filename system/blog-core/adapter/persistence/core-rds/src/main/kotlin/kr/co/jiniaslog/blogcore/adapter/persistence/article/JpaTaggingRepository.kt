package kr.co.jiniaslog.blogcore.adapter.persistence.article

import org.springframework.data.jpa.repository.JpaRepository

interface JpaTaggingRepository : JpaRepository<TaggingPM, Long>

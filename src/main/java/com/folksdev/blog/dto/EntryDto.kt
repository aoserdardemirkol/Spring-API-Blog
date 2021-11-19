package com.folksdev.blog.dto

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

data class EntryDto @JvmOverloads constructor(
    val id: String?,
    val title: String,
    val content: String,
    val createDate: LocalDateTime,
    val updateDate: LocalDateTime?,
    val tags: List<TagDto>? = ArrayList(),
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val users: UsersDto? = null,
    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val comments: List<CommentDto>? = ArrayList()
)
package com.folksdev.blog.dto

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

data class CommentDto @JvmOverloads constructor(
    val id: String?,
    val content: String,
    val createDate: LocalDateTime,
    val updateDate: LocalDateTime?,
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val entry: EntryDto? = null,
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val author: UsersDto? = null
)
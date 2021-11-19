package com.folksdev.blog.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.folksdev.blog.model.Gender

data class UsersDto @JvmOverloads constructor(
    val id: String?,
    val username: String,
    val gender: Gender,
    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val entries: List<EntryDto>? = ArrayList(),
    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val comments: List<CommentDto>? = ArrayList()
)

package com.folksdev.blog.dto.request

import javax.validation.constraints.NotBlank

data class CreateCommentRequest(
    @field:NotBlank
    val content: String,
    @field:NotBlank(message = "user can not be blank")
    val authorId: String,
    @field:NotBlank(message = "entry can not be blank")
    val entryId: String
)

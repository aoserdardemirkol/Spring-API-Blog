package com.folksdev.blog.dto.request

import javax.validation.constraints.NotBlank

data class UpdateCommentRequest(
    @field:NotBlank
    val content: String,
)

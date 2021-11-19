package com.folksdev.blog.dto.request

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

data class CreateEntryRequest(
    @field:NotBlank
    val title: String,
    @field:NotBlank
    val content: String,
    @field:NotEmpty
    val tagIds: List<String>,
    @field:NotBlank(message = "user can not be blank")
    val authorId: String
)

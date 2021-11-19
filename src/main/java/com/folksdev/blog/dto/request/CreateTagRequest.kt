package com.folksdev.blog.dto.request

import javax.validation.constraints.NotBlank

data class CreateTagRequest(
    @field:NotBlank
    val name: String,
)

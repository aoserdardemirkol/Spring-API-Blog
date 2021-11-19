package com.folksdev.blog.dto.request

import javax.validation.constraints.NotBlank

data class UpdateTagRequest constructor(
    @field:NotBlank
    val name: String,
)

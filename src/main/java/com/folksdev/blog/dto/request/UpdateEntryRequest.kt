package com.folksdev.blog.dto.request

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

data class UpdateEntryRequest constructor(
    @field:NotBlank
    var title: String,
    @field:NotBlank
    var content: String,
    @field:NotEmpty
    var tagIds:List<String>
)

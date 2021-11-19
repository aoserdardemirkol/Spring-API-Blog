package com.folksdev.blog.dto.request

import com.folksdev.blog.model.Gender
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class CreateUsersRequest(
    @field:NotBlank
    val username: String,
    @field:NotNull
    val gender: Gender
)

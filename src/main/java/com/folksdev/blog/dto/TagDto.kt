package com.folksdev.blog.dto

import com.fasterxml.jackson.annotation.JsonInclude

data class TagDto @JvmOverloads constructor(
    val id: String?,
    val name: String,
    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val entries: List<EntryDto>? = ArrayList()
)

package com.copperleaf.scorepad.models.api

import kotlinx.serialization.Serializable

@Serializable
data class StaticPage(
    val title: String = "",
    val slug: String = "",
    val content: String = "",
)

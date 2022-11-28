package com.caseyjbrooks.scorepad.ui.pages

import com.copperleaf.scorepad.models.api.StaticPage
import com.copperleaf.ballast.repository.cache.Cached

object StaticPageContract {
    data class State(
        val slug: String = "",
        val content: Cached<StaticPage> = Cached.NotLoaded(),
    )

    sealed class Inputs {
        data class Initialize(val slug: String) : Inputs()
        data class StaticPageContentUpdated(val content: Cached<StaticPage>) : Inputs()
    }

    sealed class Events
}

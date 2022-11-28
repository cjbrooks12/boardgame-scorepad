package com.caseyjbrooks.scorepad.ui.layout

import com.caseyjbrooks.scorepad.utils.theme.layouts.MainLayoutState
import com.copperleaf.scorepad.models.api.GameTypeList
import com.copperleaf.ballast.repository.cache.Cached

object LayoutContract {
    data class State(
        val layout: Cached<MainLayoutState> = Cached.NotLoaded(),
    )

    sealed class Inputs {
        object Initialize : Inputs()
        data class GameTypesUpdated(val gameTypes: Cached<GameTypeList>) : Inputs()
    }

    sealed class Events

}

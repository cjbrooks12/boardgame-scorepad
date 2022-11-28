package com.caseyjbrooks.scorepad.ui.home

import com.caseyjbrooks.scorepad.utils.theme.layouts.MainLayoutState

object HomeContract {
    data class State(
        val mainLayoutState: MainLayoutState,
    )

    sealed class Inputs {
        object Initialize : Inputs()
    }

    sealed class Events

}

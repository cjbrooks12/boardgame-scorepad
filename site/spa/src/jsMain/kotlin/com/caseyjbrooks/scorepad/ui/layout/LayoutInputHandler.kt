package com.caseyjbrooks.scorepad.ui.layout

import com.caseyjbrooks.scorepad.repository.main.Repository
import com.caseyjbrooks.scorepad.utils.theme.layouts.MainLayoutState
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import kotlinx.coroutines.flow.map

class LayoutInputHandler(
    private val repository: Repository,
) : InputHandler<
    LayoutContract.Inputs,
    LayoutContract.Events,
    LayoutContract.State> {

    override suspend fun InputHandlerScope<LayoutContract.Inputs, LayoutContract.Events, LayoutContract.State>.handleInput(
        input: LayoutContract.Inputs
    ) = when (input) {
        is LayoutContract.Inputs.Initialize -> {
            observeFlows(
                "Layout",
                repository
                    .getGameTypes(false)
                    .map { LayoutContract.Inputs.GameTypesUpdated(it) }
            )
        }

        is LayoutContract.Inputs.GameTypesUpdated -> {
            updateState {
                it.copy(
                    layout = MainLayoutState.fromCached(input.gameTypes)
                )
            }
        }
    }
}

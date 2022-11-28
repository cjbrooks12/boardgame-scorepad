package com.caseyjbrooks.scorepad.ui.home

import com.caseyjbrooks.scorepad.repository.main.Repository
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope

class HomeInputHandler(
    private val repository: Repository,
) : InputHandler<
    HomeContract.Inputs,
    HomeContract.Events,
    HomeContract.State> {

    override suspend fun InputHandlerScope<HomeContract.Inputs, HomeContract.Events, HomeContract.State>.handleInput(
        input: HomeContract.Inputs
    ) = when (input) {
        is HomeContract.Inputs.Initialize -> {
            noOp()
        }
    }
}

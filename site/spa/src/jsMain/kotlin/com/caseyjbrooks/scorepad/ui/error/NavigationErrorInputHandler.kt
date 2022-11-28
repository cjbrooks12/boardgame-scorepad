package com.caseyjbrooks.scorepad.ui.error

import com.caseyjbrooks.scorepad.repository.main.Repository
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope

class NavigationErrorInputHandler(
    private val repository: Repository,
) : InputHandler<
    NavigationErrorContract.Inputs,
    NavigationErrorContract.Events,
    NavigationErrorContract.State> {

    override suspend fun InputHandlerScope<NavigationErrorContract.Inputs, NavigationErrorContract.Events, NavigationErrorContract.State>.handleInput(
        input: NavigationErrorContract.Inputs
    ) = when (input) {
        is NavigationErrorContract.Inputs.Initialize -> {
            noOp()
        }
    }
}

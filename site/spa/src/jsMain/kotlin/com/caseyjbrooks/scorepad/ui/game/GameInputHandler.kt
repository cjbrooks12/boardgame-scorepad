package com.caseyjbrooks.scorepad.ui.game

import com.caseyjbrooks.scorepad.repository.main.Repository
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import kotlinx.coroutines.flow.map

class GameInputHandler(
    private val repository: Repository,
) : InputHandler<
    GameContract.Inputs,
    GameContract.Events,
    GameContract.State> {

    override suspend fun InputHandlerScope<GameContract.Inputs, GameContract.Events, GameContract.State>.handleInput(
        input: GameContract.Inputs
    ) = when (input) {
        is GameContract.Inputs.Initialize -> {
            val currentState = getCurrentState()
            observeFlows(
                "gameSchema",
                repository
                    .getGameInfo(false, gameId = currentState.gameId)
                    .map { GameContract.Inputs.GameInfoLoaded(it) },
                repository
                    .getFormDefinition(false, slug = currentState.gameId.id)
                    .map { GameContract.Inputs.FormDefinitionLoaded(it) }
            )
        }

        is GameContract.Inputs.GameInfoLoaded -> {
            updateState { it.copy(gameInfo = input.gameInfo) }
        }

        is GameContract.Inputs.FormDefinitionLoaded -> {
            updateState { it.copy(form = input.form) }
        }

        is GameContract.Inputs.FormDataUpdated -> {
            updateState { it.copy(data = input.data) }
        }
    }
}

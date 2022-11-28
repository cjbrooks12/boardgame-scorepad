package com.caseyjbrooks.scorepad.ui.game

import com.caseyjbrooks.scorepad.utils.form.FormDefinition
import com.copperleaf.scorepad.models.api.GameId
import com.copperleaf.scorepad.models.api.GameType
import com.copperleaf.ballast.repository.cache.Cached
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull

object GameContract {
    data class State(
        val gameId: GameId,
        val gameInfo: Cached<GameType> = Cached.NotLoaded(),
        val form: Cached<FormDefinition> = Cached.NotLoaded(),
        val data: JsonElement = JsonNull,
    )

    sealed class Inputs {
        object Initialize : Inputs()
        data class GameInfoLoaded(val gameInfo: Cached<GameType>) : Inputs()
        data class FormDefinitionLoaded(val form: Cached<FormDefinition>) : Inputs()
        data class FormDataUpdated(val data: JsonElement) : Inputs()
    }

    sealed class Events

}

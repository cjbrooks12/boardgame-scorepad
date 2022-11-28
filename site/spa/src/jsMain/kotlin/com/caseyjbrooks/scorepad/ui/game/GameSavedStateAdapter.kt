package com.caseyjbrooks.scorepad.ui.game

import com.copperleaf.scorepad.models.api.GameId
import com.copperleaf.ballast.savedstate.RestoreStateScope
import com.copperleaf.ballast.savedstate.SaveStateScope
import com.copperleaf.ballast.savedstate.SavedStateAdapter
import com.copperleaf.json.utils.parseJson
import com.copperleaf.json.utils.toJsonString
import kotlinx.browser.window
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import org.w3c.dom.get
import org.w3c.dom.set

class GameSavedStateAdapter(
    private val gameId: GameId,
) : SavedStateAdapter<
    GameContract.Inputs,
    GameContract.Events,
    GameContract.State,
    > {

    private val jsonConfig = Json {
        allowSpecialFloatingPointValues = true
        prettyPrint = false
        isLenient = true
        encodeDefaults = false
        ignoreUnknownKeys = true
        coerceInputValues = true
        explicitNulls = false
    }

    override suspend fun SaveStateScope<
        GameContract.Inputs,
        GameContract.Events,
        GameContract.State>.save() {
        saveDiff({ data }) {
            window.localStorage["formData[${gameId.id}]"] = it.toJsonString(jsonConfig)
        }
    }

    override suspend fun RestoreStateScope<
        GameContract.Inputs,
        GameContract.Events,
        GameContract.State>.restore(): GameContract.State {
        val formData: JsonElement = window.localStorage["formData[${gameId.id}]"]
            ?.parseJson(jsonConfig) ?: JsonNull
        return GameContract.State(
            gameId = gameId,
            data = formData,
        )
    }

    override suspend fun onRestoreComplete(restoredState: GameContract.State): GameContract.Inputs {
        return GameContract.Inputs.Initialize
    }
}

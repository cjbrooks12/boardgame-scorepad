package com.caseyjbrooks.scorepad.ui.game

import androidx.compose.runtime.*
import com.caseyjbrooks.scorepad.di.AppInjector
import com.caseyjbrooks.scorepad.ui.ScorepadApp
import com.caseyjbrooks.scorepad.ui.game.form.GameForm
import com.caseyjbrooks.scorepad.utils.CacheReady
import com.caseyjbrooks.scorepad.utils.theme.bulma.*
import com.caseyjbrooks.scorepad.utils.theme.layouts.MainLayoutState
import com.copperleaf.ballast.navigation.routing.directions
import com.copperleaf.ballast.navigation.routing.path
import com.copperleaf.scorepad.models.api.GameId
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

@Suppress("UNUSED_PARAMETER")
object GameUi {
    @Composable
    fun Page(injector: AppInjector, mainLayoutState: MainLayoutState, gameId: GameId) {
        val coroutineScope = rememberCoroutineScope()
        val vm = remember(coroutineScope, injector, mainLayoutState) {
            injector.gameViewModel(
                coroutineScope,
                gameId,
            )
        }
        val vmState by vm.observeStates().collectAsState()
        Page(vmState) { vm.trySend(it) }
    }

    @Composable
    fun Page(state: GameContract.State, postInput: (GameContract.Inputs) -> Unit) {
        Div({
            classes("game-page", state.gameId.id)
        }) {
            Header(state)
            Card {
                Body(state, postInput)
            }
        }
    }

    @Composable
    fun Header(state: GameContract.State) {
        CacheReady(state.gameInfo) { gameInfo ->
            Hero(
                title = { Text(gameInfo.name) },
                size = BulmaSize.Small,
                classes = listOf("special"),
            )
            BulmaSection {
                Breadcrumbs(
                    NavigationRoute("Home", null, ScorepadApp.Home.directions()),
                    NavigationRoute(state.gameId.id, null, ScorepadApp.Game.directions().path(state.gameId.id)),
                )
            }
        }
    }

    @Composable
    fun Body(state: GameContract.State, postInput: (GameContract.Inputs) -> Unit) {
        CacheReady(state.form) { form ->
            GameForm(
                schema = form.schema,
                uiSchema = form.uiSchema,
                data = state.data,
                onDataChanged = {
                    postInput(GameContract.Inputs.FormDataUpdated(it))
                }
            )
        }
    }
}

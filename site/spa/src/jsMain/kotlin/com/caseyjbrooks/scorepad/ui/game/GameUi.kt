package com.caseyjbrooks.scorepad.ui.game

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.caseyjbrooks.scorepad.di.AppInjector
import com.caseyjbrooks.scorepad.ui.ScorepadApp
import com.caseyjbrooks.scorepad.ui.game.form.GameForm
import com.caseyjbrooks.scorepad.utils.CacheReady
import com.caseyjbrooks.scorepad.utils.theme.bulma.Breadcrumbs
import com.caseyjbrooks.scorepad.utils.theme.bulma.BulmaSection
import com.caseyjbrooks.scorepad.utils.theme.bulma.BulmaSize
import com.caseyjbrooks.scorepad.utils.theme.bulma.Card
import com.caseyjbrooks.scorepad.utils.theme.bulma.Hero
import com.caseyjbrooks.scorepad.utils.theme.bulma.NavigationRoute
import com.caseyjbrooks.scorepad.utils.theme.layouts.MainLayoutState
import com.copperleaf.scorepad.models.api.GameId
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
        Header(state)
        Card {
            Body(state, postInput)
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
                    NavigationRoute("Home", null, ScorepadApp.Home),
                    NavigationRoute(state.gameId.id, null, ScorepadApp.Game, state.gameId.id),
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

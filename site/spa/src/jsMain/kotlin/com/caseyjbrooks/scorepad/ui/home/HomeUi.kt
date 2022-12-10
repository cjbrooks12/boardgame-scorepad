package com.caseyjbrooks.scorepad.ui.home

import androidx.compose.runtime.*
import com.caseyjbrooks.scorepad.di.AppInjector
import com.caseyjbrooks.scorepad.ui.ScorepadApp
import com.caseyjbrooks.scorepad.utils.DynamicGrid
import com.caseyjbrooks.scorepad.utils.GridItem
import com.caseyjbrooks.scorepad.utils.navigation.NavigationLink
import com.caseyjbrooks.scorepad.utils.theme.bulma.*
import com.caseyjbrooks.scorepad.utils.theme.layouts.MainLayoutState
import com.copperleaf.ballast.navigation.routing.directions
import com.copperleaf.ballast.navigation.routing.path
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Ul

@Suppress("UNUSED_PARAMETER")
object HomeUi {
    @Composable
    fun Page(injector: AppInjector, mainLayoutState: MainLayoutState) {
        val coroutineScope = rememberCoroutineScope()
        val vm = remember(coroutineScope, injector, mainLayoutState) {
            injector.homeViewModel(
                coroutineScope,
                mainLayoutState
            )
        }
        val vmState by vm.observeStates().collectAsState()
        Page(vmState) { vm.trySend(it) }
    }

    @Composable
    fun Page(state: HomeContract.State, postInput: (HomeContract.Inputs) -> Unit) {
        Header()
        DynamicGrid(
            GridItem {
                Card {
                    Body(state, postInput)
                }
            }
        )
    }

    @Composable
    fun Header() {
        Hero(
            title = { Text("Scorepad Repository") },
            subtitle = { Text("Digital scorepads for popular boardgame end-game scoring calculations") },
            size = BulmaSize.Medium,
        )
        BulmaSection {
            Breadcrumbs(
                NavigationRoute("Home", null, ScorepadApp.Home.directions()),
            )
        }
    }

    @Composable
    fun Body(state: HomeContract.State, postInput: (HomeContract.Inputs) -> Unit) {
        Ul {
            state.mainLayoutState.gameTypes.forEach { gameType ->
                Li {
                    NavigationLink(
                        NavigationRoute(
                            name = gameType.name,
                            iconUrl = null,
                            directions = ScorepadApp.Game
                                .directions()
                                .path(gameType.id.id),
                        ),
                    ) {
                        Text(gameType.name)
                    }
                }
            }
        }
    }
}

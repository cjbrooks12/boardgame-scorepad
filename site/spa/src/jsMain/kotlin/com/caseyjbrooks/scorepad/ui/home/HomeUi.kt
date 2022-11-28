package com.caseyjbrooks.scorepad.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.caseyjbrooks.scorepad.di.AppInjector
import com.caseyjbrooks.scorepad.ui.ScorepadApp
import com.caseyjbrooks.scorepad.utils.DynamicGrid
import com.caseyjbrooks.scorepad.utils.GridItem
import com.caseyjbrooks.scorepad.utils.navigation.NavigationLink
import com.caseyjbrooks.scorepad.utils.theme.bulma.Breadcrumbs
import com.caseyjbrooks.scorepad.utils.theme.bulma.BulmaSection
import com.caseyjbrooks.scorepad.utils.theme.bulma.BulmaSize
import com.caseyjbrooks.scorepad.utils.theme.bulma.Card
import com.caseyjbrooks.scorepad.utils.theme.bulma.Hero
import com.caseyjbrooks.scorepad.utils.theme.bulma.NavigationRoute
import com.caseyjbrooks.scorepad.utils.theme.layouts.MainLayoutState
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
                NavigationRoute("Home", null, ScorepadApp.Home),
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
                            route = ScorepadApp.Game,
                            pathParams = arrayOf(gameType.id.id),
                        ),
                    ) {
                        Text(gameType.name)
                    }
                }
            }
        }
    }
}

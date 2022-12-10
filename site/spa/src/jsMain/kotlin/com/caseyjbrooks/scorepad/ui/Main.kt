package com.caseyjbrooks.scorepad.ui

import androidx.compose.runtime.*
import com.caseyjbrooks.scorepad.di.AppInjector
import com.caseyjbrooks.scorepad.ui.error.NavigationErrorUi
import com.caseyjbrooks.scorepad.ui.game.GameUi
import com.caseyjbrooks.scorepad.ui.home.HomeUi
import com.caseyjbrooks.scorepad.ui.layout.LayoutUi
import com.caseyjbrooks.scorepad.ui.pages.StaticPageUi
import com.caseyjbrooks.scorepad.utils.theme.AppTheme
import com.caseyjbrooks.scorepad.utils.theme.layouts.MainLayoutState
import com.copperleaf.ballast.navigation.routing.Destination
import com.copperleaf.ballast.navigation.routing.renderCurrentDestination
import com.copperleaf.ballast.navigation.routing.stringPath
import com.copperleaf.scorepad.models.api.GameId
import org.jetbrains.compose.web.dom.Text

val LocalInjector = staticCompositionLocalOf<AppInjector> { error("LocalInjector not provided") }

@Composable
fun MainApplication(injector: AppInjector) {
    AppTheme(injector) {
        LayoutUi.Page(injector) { mainLayoutState ->
            val routerVm = remember(injector) { injector.routerViewModel() }
            val routerVmState by routerVm.observeStates().collectAsState()

            routerVmState.renderCurrentDestination(
                route = {
                    MainApplicationRouteMatch(injector, mainLayoutState)
                },
                notFound = {
                    NavigationErrorUi.Page(injector) {
                        Text("$it not found")
                    }
                },
            )
        }
    }
}

@Composable
fun Destination.Match<ScorepadApp>.MainApplicationRouteMatch(injector: AppInjector, mainLayoutState: MainLayoutState) {
    when (originalRoute) {
        ScorepadApp.Home -> {
            HomeUi.Page(injector, mainLayoutState)
        }

        ScorepadApp.StaticPage -> {
            val slug by stringPath()
            StaticPageUi.Page(
                injector,
                slug,
            )
        }

        ScorepadApp.Game -> {
            val gameId by stringPath()
            GameUi.Page(injector, mainLayoutState, GameId(gameId))
        }
    }
}

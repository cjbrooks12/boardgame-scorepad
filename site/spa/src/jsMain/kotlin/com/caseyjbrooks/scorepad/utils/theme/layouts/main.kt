package com.caseyjbrooks.scorepad.utils.theme.layouts

import androidx.compose.runtime.Composable
import com.caseyjbrooks.scorepad.app.BuildConfig
import com.caseyjbrooks.scorepad.ui.ScorepadApp
import com.caseyjbrooks.scorepad.utils.CacheReady
import com.caseyjbrooks.scorepad.utils.theme.bulma.BulmaFooter
import com.caseyjbrooks.scorepad.utils.theme.bulma.MainNavBar
import com.caseyjbrooks.scorepad.utils.theme.bulma.NavigationRoute
import com.caseyjbrooks.scorepad.utils.theme.bulma.NavigationSection
import com.copperleaf.scorepad.models.api.GameTypeList
import com.copperleaf.scorepad.models.api.GameTypeLite
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.map

data class MainLayoutState(
    val brandImage: String,
    val gameTypes: List<GameTypeLite>,
    val startNavigation: List<NavigationSection>,
    val endNavigation: List<NavigationSection>,
) {
    companion object {
        fun fromCached(expansions: Cached<GameTypeList>): Cached<MainLayoutState> {
            return expansions.map { fromValue(it) }
        }

        fun fromValue(gameTypes: GameTypeList): MainLayoutState {
            return MainLayoutState(
                brandImage = "${BuildConfig.BASE_URL}/assets/main-logo.png",
                gameTypes = gameTypes.gameTypes,
                startNavigation = listOf(

                ),
                endNavigation = listOf(
                    NavigationSection(
                        "Resources",
                        NavigationRoute("Community Resources", null, ScorepadApp.StaticPage, "resources"),
                    ),
                    NavigationSection(
                        "About",
                        NavigationRoute("Resources", null, ScorepadApp.StaticPage, "about"),
                    ),
                    NavigationSection(
                        "API",
                        NavigationRoute("API", null, ScorepadApp.StaticPage, "api"),
                    ),
                )
            )
        }
    }
}

@Composable
fun MainLayout(
    cached: Cached<MainLayoutState>,
    content: @Composable (MainLayoutState) -> Unit
) {
    CacheReady(cached) { layoutState ->
        MainNavBar(
            homeRoute = NavigationRoute("Home", null, ScorepadApp.Home),
            brandImageUrl = layoutState.brandImage,
            startNavigation = layoutState.startNavigation,
            endNavigation = layoutState.endNavigation,
        )
        content(layoutState)
        BulmaFooter {

        }
    }
}

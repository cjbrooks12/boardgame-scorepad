package com.caseyjbrooks.scorepad.ui.pages

import androidx.compose.runtime.*
import com.caseyjbrooks.scorepad.di.AppInjector
import com.caseyjbrooks.scorepad.ui.ScorepadApp
import com.caseyjbrooks.scorepad.utils.CacheReady
import com.caseyjbrooks.scorepad.utils.DynamicGrid
import com.caseyjbrooks.scorepad.utils.GridItem
import com.caseyjbrooks.scorepad.utils.theme.bulma.*
import com.copperleaf.ballast.navigation.routing.directions
import com.copperleaf.ballast.navigation.routing.path
import com.copperleaf.scorepad.models.api.StaticPage
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

@Suppress("UNUSED_PARAMETER")
object StaticPageUi {
    @Composable
    fun Page(injector: AppInjector, slug: String) {
        val coroutineScope = rememberCoroutineScope()
        val vm = remember(coroutineScope, injector, slug) { injector.staticPageViewModel(coroutineScope, slug) }
        val vmState by vm.observeStates().collectAsState()
        Page(vmState) { vm.trySend(it) }
    }

    @Composable
    fun Page(state: StaticPageContract.State, postInput: (StaticPageContract.Inputs) -> Unit) {
        CacheReady(state.content) { page ->
            Header(page)
            DynamicGrid(
                GridItem {
                    Card {
                        Body(page)
                    }
                }
            )
        }
    }

    @Composable
    fun Header(page: StaticPage) {
        Hero(
            title = { Text(page.title) },
            size = BulmaSize.Medium,
        )
        BulmaSection {
            Breadcrumbs(
                NavigationRoute("Home", null, ScorepadApp.Home.directions()),
                NavigationRoute(page.title, null, ScorepadApp.StaticPage.directions().path(page.slug)),
            )
        }
    }

    @Composable
    fun Body(page: StaticPage) {
        BulmaSection {
            Card(
                content = {
                    Div(attrs = {
                        ref { element ->
                            element.innerHTML = page.content
                            onDispose {}
                        }
                    })
                }
            )
        }
    }
}

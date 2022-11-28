package com.caseyjbrooks.scorepad.di

import com.caseyjbrooks.scorepad.ui.error.NavigationErrorContract
import com.caseyjbrooks.scorepad.ui.error.NavigationErrorInputHandler
import com.caseyjbrooks.scorepad.ui.error.NavigationErrorViewModel
import com.caseyjbrooks.scorepad.ui.game.GameContract
import com.caseyjbrooks.scorepad.ui.game.GameInputHandler
import com.caseyjbrooks.scorepad.ui.game.GameSavedStateAdapter
import com.caseyjbrooks.scorepad.ui.game.GameViewModel
import com.caseyjbrooks.scorepad.ui.home.HomeContract
import com.caseyjbrooks.scorepad.ui.home.HomeInputHandler
import com.caseyjbrooks.scorepad.ui.home.HomeViewModel
import com.caseyjbrooks.scorepad.ui.layout.LayoutContract
import com.caseyjbrooks.scorepad.ui.layout.LayoutInputHandler
import com.caseyjbrooks.scorepad.ui.layout.LayoutViewModel
import com.caseyjbrooks.scorepad.ui.pages.StaticPageContract
import com.caseyjbrooks.scorepad.ui.pages.StaticPageInputHandler
import com.caseyjbrooks.scorepad.ui.pages.StaticPageViewModel
import com.caseyjbrooks.scorepad.utils.theme.layouts.MainLayoutState
import com.copperleaf.scorepad.models.api.GameId
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.eventHandler
import com.copperleaf.ballast.plusAssign
import com.copperleaf.ballast.savedstate.BallastSavedStateInterceptor
import kotlinx.coroutines.CoroutineScope

class ViewModelScopeImpl(
    override val repositoryScope: RepositoryScope,
) : ViewModelScope {
    override fun layoutViewModel(coroutineScope: CoroutineScope): LayoutViewModel {
        return BasicViewModel(
            coroutineScope = coroutineScope,
            config = repositoryScope.singletonScope.defaultConfig(
                initialState = LayoutContract.State(),
                inputHandler = LayoutInputHandler(
                    repository = repositoryScope.repository()
                ),
                name = "Navigation Error",
            ) { LayoutContract.Inputs.Initialize },
            eventHandler = eventHandler { },
        )
    }

    override fun navigationErrorViewModel(
        coroutineScope: CoroutineScope
    ): NavigationErrorViewModel {
        return BasicViewModel(
            coroutineScope = coroutineScope,
            config = repositoryScope.singletonScope.defaultConfig(
                initialState = NavigationErrorContract.State(),
                inputHandler = NavigationErrorInputHandler(
                    repository = repositoryScope.repository()
                ),
                name = "Navigation Error",
            ) { NavigationErrorContract.Inputs.Initialize },
            eventHandler = eventHandler { },
        )
    }

    override fun homeViewModel(
        coroutineScope: CoroutineScope,
        mainLayoutState: MainLayoutState,
    ): HomeViewModel {
        return BasicViewModel(
            coroutineScope = coroutineScope,
            config = repositoryScope.singletonScope.defaultConfig(
                inputHandler = HomeInputHandler(
                    repository = repositoryScope.repository()
                ),
                initialState = HomeContract.State(mainLayoutState),
                name = "Home",
            ) { HomeContract.Inputs.Initialize },
            eventHandler = eventHandler { },
        )
    }

    override fun staticPageViewModel(
        coroutineScope: CoroutineScope,
        slug: String,
    ): StaticPageViewModel {
        return BasicViewModel(
            coroutineScope = coroutineScope,
            config = repositoryScope.singletonScope.defaultConfig(
                inputHandler = StaticPageInputHandler(
                    repository = repositoryScope.repository()
                ),
                initialState = StaticPageContract.State(),
                name = "Static Page",
            ) { StaticPageContract.Inputs.Initialize(slug) },
            eventHandler = eventHandler { },
        )
    }

    override fun gameViewModel(coroutineScope: CoroutineScope, gameId: GameId): GameViewModel {
        return BasicViewModel(
            coroutineScope = coroutineScope,
            config = repositoryScope.singletonScope.defaultConfig(
                inputHandler = GameInputHandler(
                    repository = repositoryScope.repository(),
                ),
                initialState = GameContract.State(gameId = gameId),
                name = "Game(${gameId.id})",
                additionalConfig = {
                    this += BallastSavedStateInterceptor(
                        GameSavedStateAdapter(gameId)
                    )
                }
            ),
            eventHandler = eventHandler { },
        )
    }
}


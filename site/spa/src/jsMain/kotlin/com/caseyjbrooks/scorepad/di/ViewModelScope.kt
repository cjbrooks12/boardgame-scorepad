package com.caseyjbrooks.scorepad.di

import com.caseyjbrooks.scorepad.ui.error.NavigationErrorViewModel
import com.caseyjbrooks.scorepad.ui.game.GameViewModel
import com.caseyjbrooks.scorepad.ui.home.HomeViewModel
import com.caseyjbrooks.scorepad.ui.layout.LayoutViewModel
import com.caseyjbrooks.scorepad.ui.pages.StaticPageViewModel
import com.caseyjbrooks.scorepad.utils.theme.layouts.MainLayoutState
import com.copperleaf.scorepad.models.api.GameId
import kotlinx.coroutines.CoroutineScope

interface ViewModelScope {
    val repositoryScope: RepositoryScope

    fun layoutViewModel(coroutineScope: CoroutineScope): LayoutViewModel
    fun navigationErrorViewModel(coroutineScope: CoroutineScope): NavigationErrorViewModel

    fun homeViewModel(coroutineScope: CoroutineScope, mainLayoutState: MainLayoutState): HomeViewModel
    fun staticPageViewModel(coroutineScope: CoroutineScope, slug: String): StaticPageViewModel
    fun gameViewModel(coroutineScope: CoroutineScope, gameId: GameId): GameViewModel
}

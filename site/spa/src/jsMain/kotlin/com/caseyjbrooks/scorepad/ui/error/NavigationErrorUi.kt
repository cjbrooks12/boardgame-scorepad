package com.caseyjbrooks.scorepad.ui.error

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.caseyjbrooks.scorepad.di.AppInjector
import com.caseyjbrooks.scorepad.utils.DynamicGrid
import com.caseyjbrooks.scorepad.utils.GridItem
import com.caseyjbrooks.scorepad.utils.theme.bulma.Card

object NavigationErrorUi {
    @Composable
    fun Page(injector: AppInjector, content: @Composable () -> Unit) {
        val coroutineScope = rememberCoroutineScope()
        val vm = remember(coroutineScope, injector) { injector.navigationErrorViewModel(coroutineScope) }
        val vmState by vm.observeStates().collectAsState()

        DynamicGrid(
            GridItem {
                Card {
                    content()
                }
            }
        )
    }
}

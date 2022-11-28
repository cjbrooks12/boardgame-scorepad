package com.caseyjbrooks.scorepad.ui.layout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.caseyjbrooks.scorepad.di.AppInjector
import com.caseyjbrooks.scorepad.utils.CacheReady
import com.caseyjbrooks.scorepad.utils.theme.layouts.MainLayout
import com.caseyjbrooks.scorepad.utils.theme.layouts.MainLayoutState

@Suppress("UNUSED_PARAMETER")
object LayoutUi {
    @Composable
    fun Page(injector: AppInjector, content: @Composable (MainLayoutState) -> Unit) {
        val coroutineScope = rememberCoroutineScope()
        val vm = remember(coroutineScope, injector) { injector.layoutViewModel(coroutineScope) }
        val vmState by vm.observeStates().collectAsState()
        Page(vmState, { vm.trySend(it) }, content)
    }

    @Composable
    fun Page(
        state: LayoutContract.State,
        postInput: (LayoutContract.Inputs) -> Unit,
        content: @Composable (MainLayoutState) -> Unit,
    ) {
        MainLayout(state.layout) {
            CacheReady(state.layout) { layoutState ->
                content(layoutState)
            }
        }
    }
}

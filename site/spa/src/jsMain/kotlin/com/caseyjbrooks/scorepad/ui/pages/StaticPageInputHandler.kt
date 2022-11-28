package com.caseyjbrooks.scorepad.ui.pages

import com.caseyjbrooks.scorepad.repository.main.Repository
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import kotlinx.coroutines.flow.map

class StaticPageInputHandler(
    private val repository: Repository,
) : InputHandler<
    StaticPageContract.Inputs,
    StaticPageContract.Events,
    StaticPageContract.State,
    > {
    override suspend fun InputHandlerScope<StaticPageContract.Inputs, StaticPageContract.Events, StaticPageContract.State>.handleInput(
        input: StaticPageContract.Inputs
    ) = when (input) {
        is StaticPageContract.Inputs.Initialize -> {
            updateState { it.copy(slug = input.slug) }
            observeFlows(
                "Static Page Expansions",
                repository
                    .getStaticPageContent(false, input.slug)
                    .map { cached -> StaticPageContract.Inputs.StaticPageContentUpdated(cached) }
            )
        }

        is StaticPageContract.Inputs.StaticPageContentUpdated -> {
            updateState { it.copy(content = input.content) }
        }
    }
}

package com.caseyjbrooks.scorepad.repository.main

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope

class RepositoryInputHandler : InputHandler<
    RepositoryContract.Inputs,
    RepositoryContract.Events,
    RepositoryContract.State> {
    override suspend fun InputHandlerScope<
        RepositoryContract.Inputs,
        RepositoryContract.Events,
        RepositoryContract.State>.handleInput(
        input: RepositoryContract.Inputs
    ) = when (input) {
        is RepositoryContract.Inputs.FetchCachedValue -> {
            val currentState = getCurrentState()

            val cachedValue = currentState.caches.singleOrNull { it.key == input.key }
            if(cachedValue != null) {
                // refresh the cache
                cachedValue.fetchWithCache(input.forceRefresh)
            } else {
                // add the cache and start it running
                val newCachedValue = input.newCache()
                newCachedValue.fetchWithCache(input.forceRefresh)
                updateState { it.copy(caches = (it.caches + newCachedValue).takeLast(10)) }
            }
        }
    }
}

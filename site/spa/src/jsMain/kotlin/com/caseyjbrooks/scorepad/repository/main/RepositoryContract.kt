package com.caseyjbrooks.scorepad.repository.main

object RepositoryContract {
    data class State(
        val caches: List<SimpleCachedValue<*>> = emptyList(),
    )

    sealed class Inputs {
        data class FetchCachedValue(
            val forceRefresh: Boolean,
            val key: SimpleCachedValue.Key<*>,
            val doFetch: suspend () -> Any
        ) : Inputs() {
            fun newCache(): SimpleCachedValue<*> {
                return SimpleCachedValue(key, doFetch)
            }
        }
    }
    sealed class Events {

    }
}

package com.caseyjbrooks.scorepad.repository.main

import com.caseyjbrooks.scorepad.api.AppApi
import com.caseyjbrooks.scorepad.utils.form.FormDefinition
import com.copperleaf.scorepad.models.api.GameId
import com.copperleaf.scorepad.models.api.GameType
import com.copperleaf.scorepad.models.api.GameTypeList
import com.copperleaf.scorepad.models.api.StaticPage
import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.eventHandler
import com.copperleaf.ballast.repository.cache.Cached
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest

class RepositoryImpl(
    coroutineScope: CoroutineScope,
    config: BallastViewModelConfiguration<
        RepositoryContract.Inputs,
        RepositoryContract.Events,
        RepositoryContract.State>,
    private val api: AppApi,
) : BasicViewModel<
    RepositoryContract.Inputs,
    RepositoryContract.Events,
    RepositoryContract.State>(
    coroutineScope = coroutineScope,
    config = config,
    eventHandler = eventHandler {  },
), Repository {

    override fun getGameTypes(forceRefresh: Boolean): Flow<Cached<GameTypeList>> {
        return flowOfKey(
            forceRefresh,
            SimpleCachedValue.Key("GameTypes", null)
        ) { api.getGameTypes() }
    }

    override fun getGameInfo(forceRefresh: Boolean, gameId: GameId): Flow<Cached<GameType>> {
        return flowOfKey(
            forceRefresh,
            SimpleCachedValue.Key("GameTypes", gameId.id)
        ) { api.getGameInfo(gameId) }
    }

    override fun getStaticPageContent(forceRefresh: Boolean, slug: String): Flow<Cached<StaticPage>> {
        return flowOfKey(
            forceRefresh,
            SimpleCachedValue.Key("StaticPage", slug)
        ) { api.getStaticPageContent(slug) }
    }

    override fun getFormDefinition(forceRefresh: Boolean, slug: String): Flow<Cached<FormDefinition>> {
        return flowOfKey(
            forceRefresh,
            SimpleCachedValue.Key("Form Definition", slug)
        ) { api.getFormDefinition(slug) }
    }

    // Utils
// ---------------------------------------------------------------------------------------------------------------------

    private fun <T : Any> flowOfKey(
        forceRefresh: Boolean,
        key: SimpleCachedValue.Key<T>,
        doFetch: suspend () -> T
    ): Flow<Cached<T>> {
        trySend(RepositoryContract.Inputs.FetchCachedValue(forceRefresh, key, doFetch))

        return observeStates()
            .flatMapLatest {
                it.caches.singleOrNull { it.key == key }
                    ?.asFlow()
                    ?: emptyFlow()
            }
    }
}

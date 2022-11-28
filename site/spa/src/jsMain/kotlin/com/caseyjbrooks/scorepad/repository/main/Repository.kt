package com.caseyjbrooks.scorepad.repository.main

import com.caseyjbrooks.scorepad.utils.form.FormDefinition
import com.copperleaf.scorepad.models.api.GameId
import com.copperleaf.scorepad.models.api.GameType
import com.copperleaf.scorepad.models.api.GameTypeList
import com.copperleaf.scorepad.models.api.StaticPage
import com.copperleaf.ballast.repository.cache.Cached
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun getGameTypes(forceRefresh: Boolean): Flow<Cached<GameTypeList>>
    fun getGameInfo(forceRefresh: Boolean, gameId: GameId): Flow<Cached<GameType>>

    fun getStaticPageContent(forceRefresh: Boolean, slug: String): Flow<Cached<StaticPage>>

    fun getFormDefinition(forceRefresh: Boolean, slug: String): Flow<Cached<FormDefinition>>
}

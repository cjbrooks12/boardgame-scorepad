package com.caseyjbrooks.scorepad.api

import com.caseyjbrooks.scorepad.utils.form.FormDefinition
import com.copperleaf.scorepad.models.api.GameId
import com.copperleaf.scorepad.models.api.GameType
import com.copperleaf.scorepad.models.api.GameTypeList
import com.copperleaf.scorepad.models.api.StaticPage

interface AppApi {
    suspend fun getGameTypes(): GameTypeList
    suspend fun getGameInfo(gameId: GameId): GameType

    suspend fun getStaticPageContent(slug: String): StaticPage
    suspend fun getFormDefinition(slug: String): FormDefinition
}

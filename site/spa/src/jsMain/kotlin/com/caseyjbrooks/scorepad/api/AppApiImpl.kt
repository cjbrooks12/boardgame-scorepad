package com.caseyjbrooks.scorepad.api

import com.caseyjbrooks.scorepad.config.AppConfig
import com.caseyjbrooks.scorepad.utils.form.FormDefinition
import com.copperleaf.scorepad.models.api.GameId
import com.copperleaf.scorepad.models.api.GameType
import com.copperleaf.scorepad.models.api.GameTypeList
import com.copperleaf.scorepad.models.api.StaticPage
import com.copperleaf.forms.core.ui.UiSchema
import com.copperleaf.json.schema.JsonSchema
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.JsonElement

class AppApiImpl(
    private val config: AppConfig,
    private val httpClient: HttpClient,
) : AppApi {

    override suspend fun getGameTypes(): GameTypeList {
        return httpClient
            .get("api/games.json")
            .body()
    }

    override suspend fun getGameInfo(gameId: GameId): GameType {
        return httpClient
            .get("api/schemas/${gameId.id}/info.json")
            .body()
    }

    override suspend fun getStaticPageContent(slug: String): StaticPage {
        return httpClient
            .get("api/pages/$slug.json")
            .body()
    }

    override suspend fun getFormDefinition(slug: String): FormDefinition = coroutineScope {
        val asyncSchema: Deferred<JsonElement> = async {
            httpClient.get("api/schemas/$slug/schema.json").body()
        }
        val asyncUiSchema: Deferred<JsonElement> = async {
            httpClient.get("api/schemas/$slug/uiSchema.json").body()
        }
        val asyncDefaultData: Deferred<JsonElement> = async {
            httpClient.get("api/schemas/$slug/defaultData.json").body()
        }

        val schema = JsonSchema.parse(asyncSchema.await())
        val uiSchema = UiSchema.parse(schema, asyncUiSchema.await())

        FormDefinition(
            schema = schema,
            uiSchema = uiSchema,
            defaultData = asyncDefaultData.await(),
        )
    }
}

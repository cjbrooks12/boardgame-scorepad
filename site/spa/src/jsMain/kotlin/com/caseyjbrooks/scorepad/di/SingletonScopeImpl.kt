package com.caseyjbrooks.scorepad.di

import com.caseyjbrooks.scorepad.config.AppConfig
import com.caseyjbrooks.scorepad.utils.navigation.HashNavigationLinkStrategy
import com.caseyjbrooks.scorepad.utils.navigation.HistoryNavigationLinkStrategy
import com.caseyjbrooks.scorepad.utils.navigation.NavigationLinkStrategy
import com.copperleaf.ballast.core.JsConsoleBallastLogger
import com.copperleaf.ballast.debugger.BallastDebuggerClientConnection
import com.copperleaf.ballast.repository.bus.EventBusImpl
import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.json.Json

class SingletonScopeImpl(
    override val applicationCoroutineScope: CoroutineScope,
    override val config: AppConfig,
) : SingletonScope {
    override val eventBus = EventBusImpl()
    override val httpClient = HttpClient(Js) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        defaultRequest {
            url("${config.baseUrl}/")
        }
    }

    override val ballastLogger = JsConsoleBallastLogger()

    override val navigationLinkStrategy: NavigationLinkStrategy = if (config.useHistoryApi) {
        HistoryNavigationLinkStrategy
    } else {
        HashNavigationLinkStrategy
    }

    override val debuggerConnection by lazy {
        BallastDebuggerClientConnection(
            Js,
            applicationCoroutineScope,
            host = "127.0.0.1",
        ).also { it.connect() }
    }
}


package com.caseyjbrooks.scorepad.di

import com.caseyjbrooks.scorepad.config.AppConfig
import com.caseyjbrooks.scorepad.utils.navigation.NavigationLinkStrategy
import com.copperleaf.ballast.BallastLogger
import com.copperleaf.ballast.debugger.BallastDebuggerClientConnection
import com.copperleaf.ballast.repository.bus.EventBus
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineConfig
import kotlinx.coroutines.CoroutineScope

interface SingletonScope {
    val applicationCoroutineScope: CoroutineScope
    val config: AppConfig
    val ballastLogger: BallastLogger
    val navigationLinkStrategy: NavigationLinkStrategy
    val eventBus: EventBus
    val httpClient: HttpClient
    val debuggerConnection: BallastDebuggerClientConnection<HttpClientEngineConfig>
}


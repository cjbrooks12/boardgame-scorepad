package com.caseyjbrooks.scorepad.di

import com.caseyjbrooks.scorepad.api.AppApiImpl
import com.caseyjbrooks.scorepad.repository.main.Repository
import com.caseyjbrooks.scorepad.repository.main.RepositoryContract
import com.caseyjbrooks.scorepad.repository.main.RepositoryImpl
import com.caseyjbrooks.scorepad.repository.main.RepositoryInputHandler
import com.caseyjbrooks.scorepad.ui.ScorepadApp
import com.copperleaf.ballast.build
import com.copperleaf.ballast.core.FifoInputStrategy
import com.copperleaf.ballast.eventHandler
import com.copperleaf.ballast.navigation.BasicRouter
import com.copperleaf.ballast.navigation.Router
import com.copperleaf.ballast.navigation.browser.BrowserHashNavigationInterceptor
import com.copperleaf.ballast.navigation.browser.BrowserHistoryNavigationInterceptor
import com.copperleaf.ballast.navigation.routing.NavGraph
import com.copperleaf.ballast.navigation.routing.fromEnum
import com.copperleaf.ballast.navigation.vm.RouterContract
import com.copperleaf.ballast.navigation.vm.withRouter
import com.copperleaf.ballast.plusAssign

class RepositoryScopeImpl(
    override val singletonScope: SingletonScope,
) : RepositoryScope {

    private val router: Router<ScorepadApp> = BasicRouter(
        coroutineScope = singletonScope.applicationCoroutineScope,
        config = singletonScope
            .defaultConfigBuilder<RouterContract.Inputs<ScorepadApp>, RouterContract.Events<ScorepadApp>, RouterContract.State<ScorepadApp>>(
                additionalConfig = {
                    if (singletonScope.config.useHistoryApi) {
                        this += BrowserHistoryNavigationInterceptor(singletonScope.config.basePath, ScorepadApp.Home)
                    } else {
                        this += BrowserHashNavigationInterceptor(ScorepadApp.Home)
                    }
                },
            )
            .withRouter(NavGraph.fromEnum(ScorepadApp.values()), null)
            .build(),
        eventHandler = eventHandler { },
    )

    private val repository: Repository = RepositoryImpl(
        coroutineScope = singletonScope.applicationCoroutineScope,
        config = singletonScope.defaultConfig(
            initialState = RepositoryContract.State(),
            inputHandler = RepositoryInputHandler(),
            name = "Scorepad Repository",
            additionalConfig = { inputStrategy = FifoInputStrategy() }
        ),
        api = AppApiImpl(
            config = singletonScope.config,
            httpClient = singletonScope.httpClient,
        ),
    )

    override fun routerViewModel(): Router<ScorepadApp> {
        return router
    }

    override fun repository(): Repository {
        return repository
    }
}

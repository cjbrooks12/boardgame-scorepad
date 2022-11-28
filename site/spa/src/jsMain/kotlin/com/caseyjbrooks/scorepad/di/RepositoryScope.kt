package com.caseyjbrooks.scorepad.di

import com.caseyjbrooks.scorepad.repository.main.Repository
import com.caseyjbrooks.scorepad.ui.ScorepadApp
import com.copperleaf.ballast.navigation.Router

interface RepositoryScope {
    val singletonScope: SingletonScope
    fun routerViewModel(): Router<ScorepadApp>
    fun repository(): Repository
}

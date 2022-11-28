package com.caseyjbrooks.scorepad.di

import com.caseyjbrooks.scorepad.config.AppConfig
import kotlinx.coroutines.CoroutineScope

class AppInjectorImpl(
    applicationCoroutineScope: CoroutineScope,
    config: AppConfig,
    singletonScope: SingletonScope = SingletonScopeImpl(applicationCoroutineScope, config),
    repositoryScope: RepositoryScope = RepositoryScopeImpl(singletonScope),
    viewModelScope: ViewModelScope = ViewModelScopeImpl(repositoryScope),
) : AppInjector,
    SingletonScope by singletonScope,
    RepositoryScope by repositoryScope,
    ViewModelScope by viewModelScope


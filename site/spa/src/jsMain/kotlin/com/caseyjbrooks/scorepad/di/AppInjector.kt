package com.caseyjbrooks.scorepad.di

interface AppInjector :
    SingletonScope,
    RepositoryScope,
    ViewModelScope

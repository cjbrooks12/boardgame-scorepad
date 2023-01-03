package com.caseyjbrooks.scorepad.di

import com.copperleaf.ballast.*
import com.copperleaf.ballast.core.BootstrapInterceptor
import com.copperleaf.ballast.core.LoggingInterceptor
import com.copperleaf.ballast.debugger.BallastDebuggerInterceptor

fun <Inputs : Any, Events : Any, State : Any> SingletonScope.defaultConfig(
    initialState: State,
    inputHandler: InputHandler<Inputs, Events, State>,
    useDebugger: Boolean = false,
    useLogger: Boolean = false,
    name: String? = null,
    additionalConfig: (BallastViewModelConfiguration.Builder.() -> Unit)? = null,
    getInitialInput: (suspend () -> Inputs)? = null,
): BallastViewModelConfiguration<Inputs, Events, State> {
    return defaultConfigBuilder<Inputs, Events, State>(
        useDebugger,
        useLogger,
        additionalConfig,
        getInitialInput,
    )
        .withViewModel(
            initialState = initialState,
            inputHandler = inputHandler,
            name = name,
        )
        .build()
}


fun <Inputs : Any, Events : Any, State : Any> SingletonScope.defaultConfigBuilder(
    useDebugger: Boolean = false,
    useLogger: Boolean = false,
    additionalConfig: (BallastViewModelConfiguration.Builder.() -> Unit)? = null,
    getInitialInput: (suspend () -> Inputs)? = null,
): BallastViewModelConfiguration.Builder {
    return BallastViewModelConfiguration.Builder()
        .apply {
            logger = { ballastLogger }
            if (config.debug && useLogger) {
                this += LoggingInterceptor()
            }

            if (useDebugger) {
                this += BallastDebuggerInterceptor(debuggerConnection)
            }

            if (getInitialInput != null) {
                this += BootstrapInterceptor(getInitialInput)
            }

            if (additionalConfig != null) {
                additionalConfig()
            }
        }
}

package com.caseyjbrooks.scorepad

import androidx.compose.runtime.DisposableEffect
import com.caseyjbrooks.scorepad.config.AppConfigImpl
import com.caseyjbrooks.scorepad.di.AppInjector
import com.caseyjbrooks.scorepad.di.AppInjectorImpl
import com.caseyjbrooks.scorepad.ui.MainApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import org.jetbrains.compose.web.renderComposableInBody

fun browserMain(isPwa: Boolean) {
    println("browser main")

    try {
        val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
        val injector: AppInjector = AppInjectorImpl(
            applicationScope,
            AppConfigImpl(
                isPwa = isPwa,
            )
        )

        renderComposableInBody {
            DisposableEffect(Unit) {
                onDispose { applicationScope.cancel() }
            }

            MainApplication(injector)
        }
    } catch (e: Throwable) {
        println(e.message)
        console.error(e)
    }
}

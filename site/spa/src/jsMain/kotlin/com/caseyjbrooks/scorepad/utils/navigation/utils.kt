package com.caseyjbrooks.scorepad.utils.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.caseyjbrooks.scorepad.ui.LocalInjector
import com.caseyjbrooks.scorepad.utils.theme.bulma.NavigationRoute
import com.copperleaf.ballast.navigation.routing.RouterContract
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.w3c.dom.HTMLAnchorElement

@Composable
fun NavigationLink(
    navigationRoute: NavigationRoute,
    attrs: AttrBuilderContext<HTMLAnchorElement>? = null,
    onClicked: () -> Unit = { },
    content: @Composable () -> Unit
) {
    val injector = LocalInjector.current
    val router = remember(injector) { injector.routerViewModel() }

    val href = remember(injector, navigationRoute) {
        injector.navigationLinkStrategy.createHref(
            navigationRoute.directions
        )
    }
    val destination = remember(injector, navigationRoute) {
        injector.navigationLinkStrategy.getDestination(
            navigationRoute.directions
        )
    }

    A(
        href = "${injector.config.baseUrl.trimEnd('/')}$href",
        attrs = {
            onClick {
                if (it.ctrlKey || it.metaKey) {
                    // let it propagate normally, don't handle it with the router
                } else {
                    it.preventDefault()
                    it.stopImmediatePropagation()
                    it.stopPropagation()
                    router.trySend(destination)
                }
                onClicked()
            }
            attrs?.invoke(this)
        }
    ) {
        content()
    }
}

@Composable
fun NavigationLinkBack(
    content: @Composable () -> Unit
) {
    val injector = LocalInjector.current
    val router = remember(injector) { injector.routerViewModel() }
    A(attrs = {
        onClick {
            it.preventDefault()
            it.stopPropagation()
            router.trySend(RouterContract.Inputs.GoBack())
        }
    }) { content() }
}

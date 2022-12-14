package com.caseyjbrooks.scorepad.utils.theme.bulma

import androidx.compose.runtime.*
import com.caseyjbrooks.scorepad.ui.ScorepadApp
import com.caseyjbrooks.scorepad.utils.navigation.Icon
import com.caseyjbrooks.scorepad.utils.navigation.NavigationLink
import com.copperleaf.ballast.navigation.routing.Destination
import org.jetbrains.compose.web.css.keywords.auto
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.*

class NavigationSection(
    val name: String,
    vararg val routes: NavigationRoute,
)

class NavigationRoute(
    val name: String,
    val iconUrl: String?,
    val directions: Destination.Directions<ScorepadApp>,
    val buttonColor: BulmaColor = BulmaColor.Link,
    val tooltip: String? = null,
)

// NavBar
// ---------------------------------------------------------------------------------------------------------------------

@Composable
fun MainNavBar(
    homeRoute: NavigationRoute,
    brandImageUrl: String,
    startNavigation: List<NavigationSection>,
    endNavigation: List<NavigationSection>
) {
    Nav(attrs = {
        classes("navbar", "is-primary")
        attr("role", "navigation")
        attr("aria-label", "main navigation")
    }) {
        Container {
            var mobileNavbarOpen by remember { mutableStateOf(false) }

            Div(attrs = {
                classes("navbar-brand")
            }) {
                NavigationLink(
                    homeRoute,
                    onClicked = { mobileNavbarOpen = false },
                    attrs = { classes("navbar-item") },
                ) {
                    Img(
                        attrs = {
                            attr("height", "28")
                        },
                        src = brandImageUrl
                    )
                }

                A(
                    attrs = {
                        if (mobileNavbarOpen) {
                            classes("navbar-burger", "is-active")
                        } else {
                            classes("navbar-burger")
                        }
                        attr("role", "navigation")
                        attr("aria-label", "menu")
                        attr("aria-expanded", "false")
                        attr("data-target", "navbarMain")
                        onClick {
                            mobileNavbarOpen = !mobileNavbarOpen
                        }
                    }
                ) {
                    Span(attrs = { attr("aria-hidden", "true") })
                    Span(attrs = { attr("aria-hidden", "true") })
                    Span(attrs = { attr("aria-hidden", "true") })
                }
            }

            Div(attrs = {
                if (mobileNavbarOpen) {
                    classes("navbar-menu", "is-active")
                } else {
                    classes("navbar-menu")
                }
                id("navbarMain")
            }) {
                Div(attrs = {
                    classes("navbar-start")
                }) {
                    NavbarSection(startNavigation) { mobileNavbarOpen = false }
                }
                Div(attrs = {
                    classes("navbar-end")
                }) {
                    NavbarSection(endNavigation) { mobileNavbarOpen = false }
                }
            }
        }
    }
}

@Composable
fun NavbarSection(
    sections: List<NavigationSection>,
    onLinkClicked: () -> Unit,
) {
    sections.forEach { section ->
        if (section.routes.size == 1) {
            val navigationRoute = section.routes.single()
            NavigationLink(
                navigationRoute = navigationRoute,
                attrs = { classes("navbar-item") },
                onClicked = onLinkClicked,
            ) {
//                if (navigationRoute.iconUrl != null) {
//                    Span({ classes("icon", "is-small") }) {
//                        Icon(navigationRoute.iconUrl) {
//                            classes("has-text-white")
//                        }
//                    }
//                }
                Text(section.name)
            }
        } else {
            var isExpanded by remember { mutableStateOf(false) }
            Div({
                if (isExpanded) {
                    classes("navbar-item", "has-dropdown", "is-active")
                } else {
                    classes("navbar-item", "has-dropdown")
                }
            }) {
                A(attrs = {
                    classes("navbar-link")
                    onClick { isExpanded = !isExpanded }
                }) {
                    Text(section.name)
                }

                Div({ classes("navbar-dropdown") }) {
                    section.routes.forEach { navigationRoute ->
                        NavigationLink(
                            navigationRoute = navigationRoute,
                            attrs = { classes("navbar-item") },
                            onClicked = {
                                isExpanded = false
                                onLinkClicked()
                            },
                        ) {
                            Text(navigationRoute.name)
                        }
                    }
                }
            }
        }
    }
}

// Breadcrumb
// ---------------------------------------------------------------------------------------------------------------------

@Composable
fun Breadcrumbs(
    vararg routes: NavigationRoute,
) {
    Nav({ classes("breadcrumb", "has-succeeds-separator"); attr("aria-label", "breadcrumbs") }) {
        Ul {
            routes.forEachIndexed { index, navigationRoute ->
                if (index == routes.lastIndex) {
                    Li({ classes("is-active") }) {
                        NavigationLink(
                            navigationRoute = navigationRoute,
                            attrs = {
                                attr("aria-current", "page")
                                classes("has-text-white")
                            },
                        ) {
                            if (navigationRoute.iconUrl != null) {
                                Span({ classes("icon", "is-small"); style { width(auto) } }) {
                                    Icon(navigationRoute.iconUrl) {
                                        classes("has-text-white")
                                    }
                                }
                            }
                            Text(navigationRoute.name)
                        }
                    }
                } else {
                    Li {
                        NavigationLink(
                            navigationRoute = navigationRoute,
                            attrs = { classes("has-text-success") },
                        ) {
                            if (navigationRoute.iconUrl != null) {
                                Span({ classes("icon", "is-small"); style { width(auto) } }) {
                                    Icon(navigationRoute.iconUrl) {
                                        classes("has-text-success")
                                    }
                                }
                            }
                            Text(navigationRoute.name)
                        }
                    }
                }
            }
        }
    }
}

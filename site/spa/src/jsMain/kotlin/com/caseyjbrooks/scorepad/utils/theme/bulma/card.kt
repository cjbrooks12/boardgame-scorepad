package com.caseyjbrooks.scorepad.utils.theme.bulma

import androidx.compose.runtime.Composable
import com.caseyjbrooks.scorepad.utils.navigation.Icon
import com.caseyjbrooks.scorepad.utils.navigation.NavigationLink
import com.caseyjbrooks.scorepad.utils.theme.El
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.keywords.auto
import org.jetbrains.compose.web.css.margin
import org.jetbrains.compose.web.css.marginBottom
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.*
import org.w3c.dom.HTMLDivElement


@Composable
fun Card(
    title: String? = null,
    imageUrl: String? = null,
    imageLinkDestination: NavigationRoute? = null,
    description: String? = null,
    vararg navigationRoutes: NavigationRoute,
    content: ContentBuilder<HTMLDivElement>? = null,
) {
    Div({ classes("card", "is-shady") }) {
        if (imageUrl != null) {
            if (imageLinkDestination != null) {
                NavigationLink(imageLinkDestination, { classes("card-image") }) {
                    El("figure", { classes("image", "is-square") }) {
                        Img(
                            src = imageUrl,
                            alt = title ?: "",
                            attrs = { classes("modal-button"); style { property("object-fit", "cover") } }
                        )
                    }
                }
            } else {
                Div({ classes("card-image") }) {
                    El("figure", { classes("image", "is-square") }) {
                        Img(
                            src = imageUrl,
                            alt = title ?: "",
                            attrs = { classes("modal-button"); style { property("object-fit", "cover") } }
                        )
                    }
                }
            }
        }
        Div({ classes("card-content") }) {
            Div({ classes("content") }) {
                if (title != null) {
                    H4({ }) { Text(title) }
                }
                if (description != null) {
                    P({ }) { Text(description) }
                }
                if (content != null) {
                    content()
                }

                navigationRoutes.forEachIndexed { index, navigationRoute ->
                    Div({
                        if (index != navigationRoutes.lastIndex) {
                            style { marginBottom(1.cssRem) }
                        }
                    }) {
                        NavigationLink(navigationRoute) {
                            Span({
                                classes(
                                    "button",
                                    navigationRoute.buttonColor.classes,
                                    "modal-button"
                                )
                                if(navigationRoute.tooltip != null) {
                                    title(navigationRoute.tooltip)
                                }
                            }) {
                                if (navigationRoute.iconUrl != null) {
                                    Span({
                                        classes("icon", "is-small")
                                        style {
                                            width(auto)
                                            margin(
                                                0.cssRem,
                                                1.cssRem,
                                                0.cssRem,
                                                0.cssRem
                                            )
                                        }
                                    }) {
                                        Icon(navigationRoute.iconUrl) {
                                            classes("has-text-white")
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
}

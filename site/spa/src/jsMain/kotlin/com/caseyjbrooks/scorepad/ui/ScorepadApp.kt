package com.caseyjbrooks.scorepad.ui

import com.copperleaf.ballast.navigation.routing.Route
import com.copperleaf.ballast.navigation.routing.RouteAnnotation
import com.copperleaf.ballast.navigation.routing.RouteMatcher

enum class ScorepadApp(
    routeFormat: String,
    override val annotations: Set<RouteAnnotation> = emptySet(),
) : Route {
    Home("/"),
    StaticPage("/pages/{slug}"),
    Game("/games/{gameId}");

    override val matcher: RouteMatcher = RouteMatcher.create(routeFormat)
}

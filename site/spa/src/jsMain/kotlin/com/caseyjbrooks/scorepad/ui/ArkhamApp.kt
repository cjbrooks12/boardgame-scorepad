package com.caseyjbrooks.scorepad.ui

import com.copperleaf.ballast.navigation.routing.Route
import com.copperleaf.ballast.navigation.routing.RouteAnnotation
import com.copperleaf.ballast.navigation.routing.route.RouteMatcher

enum class ScorepadApp(
    routeFormat: String,
    override val annotations: List<RouteAnnotation> = emptyList(),
) : Route {
    Home("/"),
    StaticPage("/pages/{slug}"),
    Game("/games/{gameId}");

    override val matcher: RouteMatcher = RouteMatcher.create(routeFormat)
}

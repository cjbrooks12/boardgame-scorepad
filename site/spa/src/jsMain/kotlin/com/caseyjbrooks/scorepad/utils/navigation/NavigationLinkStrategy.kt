package com.caseyjbrooks.scorepad.utils.navigation

import com.caseyjbrooks.scorepad.ui.ScorepadApp
import com.copperleaf.ballast.navigation.routing.Destination
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.copperleaf.ballast.navigation.routing.build

sealed interface NavigationLinkStrategy {
    fun createHref(
        directions: Destination.Directions<ScorepadApp>
    ): String

    fun getDestination(
        directions: Destination.Directions<ScorepadApp>
    ): RouterContract.Inputs.GoToDestination<ScorepadApp>
}

object HashNavigationLinkStrategy : NavigationLinkStrategy {
    override fun createHref(
        directions: Destination.Directions<ScorepadApp>
    ): String {
        return "#${directions.build()}"
    }

    override fun getDestination(
        directions: Destination.Directions<ScorepadApp>
    ): RouterContract.Inputs.GoToDestination<ScorepadApp> {
        return RouterContract.Inputs.GoToDestination(directions.build())
    }
}

object HistoryNavigationLinkStrategy : NavigationLinkStrategy {

    override fun createHref(
        directions: Destination.Directions<ScorepadApp>
    ): String {
        return directions.build()
    }

    override fun getDestination(
        directions: Destination.Directions<ScorepadApp>
    ): RouterContract.Inputs.GoToDestination<ScorepadApp> {
        return RouterContract.Inputs.GoToDestination(directions.build())
    }
}

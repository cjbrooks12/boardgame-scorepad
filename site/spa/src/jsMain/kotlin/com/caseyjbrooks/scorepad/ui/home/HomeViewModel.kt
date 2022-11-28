package com.caseyjbrooks.scorepad.ui.home

import com.copperleaf.ballast.BallastViewModel

typealias HomeViewModel = BallastViewModel<
    HomeContract.Inputs,
    HomeContract.Events,
    HomeContract.State>


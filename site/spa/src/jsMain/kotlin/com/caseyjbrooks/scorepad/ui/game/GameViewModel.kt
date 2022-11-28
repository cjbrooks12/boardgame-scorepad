package com.caseyjbrooks.scorepad.ui.game

import com.copperleaf.ballast.BallastViewModel

typealias GameViewModel = BallastViewModel<
    GameContract.Inputs,
    GameContract.Events,
    GameContract.State>


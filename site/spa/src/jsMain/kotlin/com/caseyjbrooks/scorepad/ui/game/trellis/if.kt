package com.caseyjbrooks.scorepad.ui.game.trellis

import com.copperleaf.trellis.base.Spek
import com.copperleaf.trellis.impl.conditionals.IfSpek
import kotlinx.serialization.json.JsonElement

fun ifSpekFactory(args: List<Spek<*, *>>) = IfSpek(
    args[0] as Spek<JsonElement, Boolean>,
    args[1] as Spek<JsonElement, Int>,
    args[2] as Spek<JsonElement, Int>,
)

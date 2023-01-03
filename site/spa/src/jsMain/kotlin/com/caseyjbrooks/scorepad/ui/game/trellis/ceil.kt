package com.caseyjbrooks.scorepad.ui.game.trellis

import com.copperleaf.trellis.base.Spek
import com.copperleaf.trellis.visitor.SpekVisitor
import com.copperleaf.trellis.visitor.visiting
import kotlinx.serialization.json.JsonElement
import kotlin.math.ceil

class CeilSpek(
    private val value: Spek<JsonElement, Double>
) : Spek<JsonElement, Int> {
    override val children: List<Spek<*, *>> = listOf(value)
    override val spekName: String = "ceil"

    override fun evaluate(visitor: SpekVisitor, candidate: JsonElement): Int {
        return visiting(visitor) {
            val playerValue: Double = value.evaluate(visitor, candidate)
            ceil(playerValue).toInt()
        }
    }
}

fun ceilSpekFactory(args: List<Spek<*, *>>): Spek<*, *> = CeilSpek(args[0] as Spek<JsonElement, Double>)

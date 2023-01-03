package com.caseyjbrooks.scorepad.ui.game.trellis

import com.copperleaf.trellis.base.Spek
import com.copperleaf.trellis.visitor.SpekVisitor
import com.copperleaf.trellis.visitor.visiting
import kotlinx.serialization.json.JsonElement

class ClampToRangeSpek(
    private val _value: Spek<JsonElement, Int>,
    private val _min: Spek<JsonElement, Int>,
    private val _max: Spek<JsonElement, Int>,
) : Spek<JsonElement, Int> {
    override val children: List<Spek<*, *>> = listOf(_value, _min, _max)
    override val spekName: String = "listOf"

    override fun evaluate(visitor: SpekVisitor, candidate: JsonElement): Int {
        return visiting(visitor) {
            val value = _value.evaluate(visitor, candidate)
            val min = _min.evaluate(visitor, candidate)
            val max = _max.evaluate(visitor, candidate)

            when {
                value > max -> max
                value < min -> min
                else -> value
            }
        }
    }
}

fun clampToRangeSpekFactory(args: List<Spek<*, *>>) = ClampToRangeSpek(
    args[0] as Spek<JsonElement, Int>,
    args[1] as Spek<JsonElement, Int>,
    args[2] as Spek<JsonElement, Int>,
)

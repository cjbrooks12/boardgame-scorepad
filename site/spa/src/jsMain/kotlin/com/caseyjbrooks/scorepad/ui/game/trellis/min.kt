package com.caseyjbrooks.scorepad.ui.game.trellis

import com.copperleaf.trellis.base.Spek
import com.copperleaf.trellis.visitor.SpekVisitor
import com.copperleaf.trellis.visitor.visiting
import kotlinx.serialization.json.JsonElement

class MinSpek(
    private val values: List<Spek<JsonElement, Int>>
) : Spek<JsonElement, Int> {
    override val children: List<Spek<*, *>> = values
    override val spekName: String = "min"

    override fun evaluate(visitor: SpekVisitor, candidate: JsonElement): Int {
        return visiting(visitor) {
            val intValues: List<Int> = values.map { it.evaluate(visitor, candidate) }
            intValues.min()
        }
    }
}

fun minSpekFactory(args: List<Spek<*, *>>): Spek<*, *> = MinSpek(args.map { it as Spek<JsonElement, Int> })

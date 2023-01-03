package com.caseyjbrooks.scorepad.ui.game.trellis

import com.copperleaf.trellis.base.Spek
import com.copperleaf.trellis.visitor.SpekVisitor
import com.copperleaf.trellis.visitor.visiting
import kotlinx.serialization.json.JsonElement

class ListOfSpek(
    private val values: List<Spek<JsonElement, Int>>
) : Spek<JsonElement, List<Int>> {
    override val children: List<Spek<*, *>> = values
    override val spekName: String = "listOf"

    override fun evaluate(visitor: SpekVisitor, candidate: JsonElement): List<Int> {
        return visiting(visitor) {
            values.map { it.evaluate(visitor, candidate) }
        }
    }
}

class IndexSpek(
    private val _index: Spek<JsonElement, Int>,
    private val _value: Spek<JsonElement, List<Int>>
) : Spek<JsonElement, Int> {
    override val children: List<Spek<*, *>> = listOf(_index, _value)
    override val spekName: String = "index"

    override fun evaluate(visitor: SpekVisitor, candidate: JsonElement): Int {
        val index = _index.evaluate(visitor, candidate)
        val value = _value.evaluate(visitor, candidate)
        return value[index]
    }
}

fun listOfSpekFactory(args: List<Spek<*, *>>) = ListOfSpek(args.map { it as Spek<JsonElement, Int> })

fun indexSpekFactory(args: List<Spek<*, *>>) = IndexSpek(
    args[0] as Spek<JsonElement, Int>,
    args[1] as Spek<JsonElement, List<Int>>,
)

package com.caseyjbrooks.scorepad.ui.game.trellis

import com.copperleaf.forms.compose.controls.ControlScope
import com.copperleaf.json.values.int
import com.copperleaf.json.values.objectAt
import com.copperleaf.json.values.optional
import com.copperleaf.kudzu.node.Node
import com.copperleaf.kudzu.parser.ParserContext
import com.copperleaf.trellis.base.Spek
import com.copperleaf.trellis.dsl.parser.SpekFactory
import com.copperleaf.trellis.dsl.parser.TrellisDslParser
import com.copperleaf.trellis.visitor.EmptyVisitor
import com.copperleaf.trellis.visitor.SpekVisitor
import kotlinx.serialization.json.JsonElement
import kotlin.math.floor

val expressionFunctions: Map<String, SpekFactory> = mapOf(
    "floor" to FloorSpek.factory,
    "min" to MinSpek.factory
)

class FloorSpek(
    private val value: Spek<JsonElement, Double>
) : Spek<JsonElement, Int> {
    override val children: List<Spek<*, *>> = listOf(value)
    override val spekName: String = "floor"

    override fun evaluate(visitor: SpekVisitor, candidate: JsonElement): Int {
        val playerValue: Double = value.evaluate(visitor, candidate)
        return floor(playerValue).toInt()
    }

    companion object {
        internal val factory: (List<Spek<*, *>>) -> Spek<*, *> = { args ->
            FloorSpek(args[0] as Spek<JsonElement, Double>)
        }
    }
}

class MinSpek(
    private val values: List<Spek<JsonElement, Int>>
) : Spek<JsonElement, Int> {
    override val children: List<Spek<*, *>> = values
    override val spekName: String = "min"

    override fun evaluate(visitor: SpekVisitor, candidate: JsonElement): Int {
        val intValues: List<Int> = values.map { it.evaluate(visitor, candidate) }
        return intValues.min()
    }

    companion object {
        internal val factory: (List<Spek<*, *>>) -> Spek<*, *> = { args ->
            MinSpek(args.map { it as Spek<JsonElement, Int> })
        }
    }
}

class PlayerScoreCategorySpek(
    private val name: String
) : Spek<JsonElement, Int> {
    override val children: List<Spek<*, *>> = emptyList()
    override val spekName: String = name

    override fun evaluate(visitor: SpekVisitor, candidate: JsonElement): Int {
        return candidate.optional { int(name) } ?: 0
    }

    companion object {
        internal val factory: (List<Spek<*, *>>) -> Spek<*, *> = { args ->
            MinSpek(args.map { it as Spek<JsonElement, Int> })
        }
    }
}

// Utils
// ---------------------------------------------------------------------------------------------------------------------

class ExpressionParser(
    val expression: String,
    private val parser: TrellisDslParser,
    private val ast: Node,
) {
    fun evaluate(data: JsonElement): Int {
        val dslSpek: Spek<JsonElement, Int> = parser.parser.evaluator.evaluate(ast) as Spek<JsonElement, Int>
        return dslSpek.evaluate(EmptyVisitor, data)
    }

    companion object
}

fun ExpressionParser.Companion.compile(controlScope: ControlScope, expression: String): ExpressionParser {
    val expressionNamedValues: Map<String, SpekFactory> = controlScope
        .control
        .schemaConfig
        .objectAt("items")
        .objectAt("properties")
        .map { (key, _) -> key to { _: List<Spek<*, *>> -> PlayerScoreCategorySpek(key) } }
        .toMap()

    val parser = TrellisDslParser(expressionFunctions + expressionNamedValues)
    val (ast, remainingParserContext) = parser.parser.parse(ParserContext.fromString(expression, skipWhitespace = true))
//    check(remainingParserContext.isEmpty())

    return ExpressionParser(
        expression = expression,
        parser = parser,
        ast = ast,
    )
}

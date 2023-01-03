package com.caseyjbrooks.scorepad.ui.game.trellis

import com.copperleaf.forms.compose.controls.ControlScope
import com.copperleaf.json.values.boolean
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
import com.copperleaf.trellis.visitor.visiting
import kotlinx.serialization.json.JsonElement

val expressionFunctions: Map<String, SpekFactory> = mapOf(
    "floor" to ::floorSpekFactory,
    "ceil" to ::ceilSpekFactory,
    "min" to ::minSpekFactory,
    "max" to ::maxSpekFactory,
    "if" to ::ifSpekFactory,
    "listOf" to ::listOfSpekFactory,
    "index" to ::indexSpekFactory,
    "clampToRange" to ::clampToRangeSpekFactory,
)

class PlayerScoreCategorySpek(
    private val name: String
) : Spek<JsonElement, Any> {
    override val children: List<Spek<*, *>> = emptyList()
    override val spekName: String = name

    override fun evaluate(visitor: SpekVisitor, candidate: JsonElement): Any {
        return visiting(visitor) {
            candidate.optional { int(name) }
                ?: candidate.optional { boolean(name) }
                ?: 0
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

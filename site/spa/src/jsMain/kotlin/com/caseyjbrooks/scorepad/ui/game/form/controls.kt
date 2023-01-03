package com.caseyjbrooks.scorepad.ui.game.form

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.caseyjbrooks.scorepad.ui.game.trellis.ExpressionParser
import com.caseyjbrooks.scorepad.ui.game.trellis.compile
import com.copperleaf.forms.compose.controls.ControlRenderer
import com.copperleaf.forms.compose.form.Registered
import com.copperleaf.forms.compose.form.UiElement
import com.copperleaf.forms.compose.form.WithArrayIndex
import com.copperleaf.forms.compose.form.uiControl
import com.copperleaf.forms.core.ArrayControl
import com.copperleaf.forms.core.BooleanControl
import com.copperleaf.forms.core.IntegerControl
import com.copperleaf.forms.core.StringControl
import com.copperleaf.forms.core.internal.resolveAsControl
import com.copperleaf.forms.core.ui.UiElement
import com.copperleaf.json.values.arrayAt
import com.copperleaf.json.values.boolean
import com.copperleaf.json.values.optional
import com.copperleaf.json.values.string
import kotlinx.serialization.json.*
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.dom.*

@Composable
public fun ArrayControl.playerTable() = uiControl(
    tester = { uiSchemaConfig.optional { boolean("table") } == true },
    rank = 100,
) {
    val playerObjects: JsonArray = getTypedValue(JsonArray(emptyList())) {
        if (it == JsonNull) {
            JsonArray(emptyList())
        } else {
            it.jsonArray
        }
    }

    val tableControls: List<UiElement.Control> = remember(control) {
        control
            .uiSchemaConfig
            .arrayAt("elements")
            .map { it.jsonObject.resolveAsControl(schema) }
    }

    Div({
        classes("table-container")
        style {
            property("text-align", "center")
        }
    }) {
        Table({
            classes("table", "is-narrow", "game-table")
        }) {
            Thead {
                Tr {
                    Th { Text("Category") }
                    playerObjects.forEachIndexed { index, _ ->
                        WithArrayIndex(index) {
                            Th {
                                val playerNameControl: UiElement.Control = remember(control) {
                                    JsonObject(
                                        mapOf(
                                            "type" to JsonPrimitive("Control"),
                                            "scope" to JsonPrimitive("#/properties/players/items/properties/name"),
                                        )
                                    ).resolveAsControl(schema)
                                }

                                UiElement(playerNameControl)
                            }
                        }
                    }

                    Th {
                        Button({
                            onClick {
                                addArrayItem(
                                    playerObjects.size, JsonObject(
                                        mapOf("name" to JsonPrimitive("Player ${playerObjects.size + 1}"))
                                    )
                                )
                            }
                        }) {
                            Text("+ Add Player")
                        }
                    }
                }
            }
            Tbody {
                Tr {
                    Td { }
                    playerObjects.forEachIndexed { index, _ ->
                        Td {
                            Button({
                                onClick {
                                    removeArrayItem(index)
                                }
                            }) {
                                Text("Remove")
                            }
                        }
                    }
                }

                tableControls.forEach { childControl ->
                    Tr {
                        Td {
                            Text(childControl.label)
                        }
                        playerObjects.forEachIndexed { index, _ ->
                            WithArrayIndex(index) {
                                Td {
                                    UiElement(childControl)
                                }
                            }
                        }
                    }
                }
            }
            Tfoot {
                Tr {
                    Td { Text("Final Score") }

                    val finalScoreExpressionString = control.uiSchemaConfig.string("finalScore")
                    val expressionEvaluator: ExpressionParser = remember(finalScoreExpressionString) {
                        ExpressionParser.compile(this@uiControl, finalScoreExpressionString)
                    }

                    playerObjects.forEachIndexed { index, currentListItemValue ->
                        WithArrayIndex(index) {
                            val finalScore: Int = remember(expressionEvaluator, currentListItemValue) {
                                expressionEvaluator.evaluate(currentListItemValue)
                            }
                            Td {
                                Input(InputType.Number) {
                                    value(finalScore)
                                    classes("input")
                                    disabled()
                                    onInput { event ->
                                        // ignore
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

public fun IntegerControl.tableControl(): Registered<UiElement.Control, ControlRenderer> = uiControl {
    val currentValue = getTypedValue(0) { it.jsonPrimitive.intOrNull }
    Input(
        type = InputType.Number,
    ) {
        value(currentValue)
        classes("input")
        if (!isEnabled) {
            disabled()
        }
        onInput { event ->
            if (event.value?.toDouble()?.isNaN() == false) {
                updateFormState(event.value?.toDouble()?.toInt() ?: 0)
            }
        }
    }
}

public fun StringControl.tableControl(): Registered<UiElement.Control, ControlRenderer> = uiControl {
    val currentValue = getTypedValue("") { it.jsonPrimitive.contentOrNull }
    Input(
        type = InputType.Text,
    ) {
        value(currentValue)
        classes("input")
        if (!isEnabled) {
            disabled()
        }
        onInput { event ->
            updateFormState(event.value ?: "")
        }
    }
}

public fun IntegerControl.computedValue(): Registered<UiElement.Control, ControlRenderer> = uiControl(
    tester = { uiSchemaConfig.optional { boolean("computed") } == true },
    rank = 100,
) {
    val currentValue = getTypedValue(0) { it.jsonPrimitive.intOrNull }
    val computedValue = control.uiSchemaConfig.string("expression")
    this.currentValue

    Text(currentValue.toString())
}

public fun BooleanControl.tableControl(): Registered<UiElement.Control, ControlRenderer> = uiControl {
    val currentValue: Boolean = getTypedValue(false) { it.jsonPrimitive.booleanOrNull }
    Input(
        type = InputType.Checkbox,
    ) {
        value(currentValue.toString())
        classes("checkbox")
        if (!isEnabled) {
            disabled()
        }
        onInput { event ->
            updateFormState(event.value)
        }
    }
}

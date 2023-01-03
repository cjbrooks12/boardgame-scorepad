package com.caseyjbrooks.scorepad.ui.game.form

import androidx.compose.runtime.*
import com.copperleaf.forms.compose.bulma.form.BulmaDesignSystem
import com.copperleaf.forms.compose.bulma.form.bulmaDefaults
import com.copperleaf.forms.compose.controls.ControlRenderer
import com.copperleaf.forms.compose.elements.UiElementRenderer
import com.copperleaf.forms.compose.form.BasicForm
import com.copperleaf.forms.compose.form.FormScopeImpl
import com.copperleaf.forms.compose.form.Registered
import com.copperleaf.forms.core.*
import com.copperleaf.forms.core.ui.UiElement
import com.copperleaf.forms.core.ui.UiSchema
import com.copperleaf.forms.core.vm.FormFieldsContract
import com.copperleaf.json.pointer.JsonPointer
import com.copperleaf.json.schema.JsonSchema
import com.copperleaf.json.schema.SchemaValidationResult
import kotlinx.serialization.json.JsonElement

@Composable
public fun GameForm(
    schema: JsonSchema,
    uiSchema: UiSchema,
    data: JsonElement,
    onDataChanged: (JsonElement) -> Unit,
) {
    var touchedProperties: Set<JsonPointer> by remember { mutableStateOf(emptySet()) }

    GameForm(
        schema = schema,
        uiSchema = uiSchema,
        data = data,
        touchedProperties = touchedProperties,
        postInputCallback = {
            val (newData, newTouchedProperties) = it.applyToState(data, touchedProperties)
            val isChanged = newData != data
            touchedProperties = newTouchedProperties

            if (isChanged) {
                onDataChanged(newData)
            }
        },
    )
}

@Composable
public fun GameForm(
    schema: JsonSchema,
    uiSchema: UiSchema,
    data: JsonElement,
    touchedProperties: Set<JsonPointer>,
    postInputCallback: (FormFieldsContract.Inputs) -> Unit,
    validationResult: SchemaValidationResult = schema.validate(data),
) {
    val formScope = FormScopeImpl(
        schema = schema,
        uiSchema = uiSchema,
        data = data,
        touchedProperties = touchedProperties,
        elements = UiElement.bulmaDefaults() + UiElement.gameFormExtras(),
        controls = UiElement.Control.gameFormExtras(),
        designSystem = BulmaDesignSystem(),
        postInputCallback = postInputCallback,
        validationResult = validationResult,
    )
    formScope.BasicForm()
}

@Composable
public fun UiElement.Companion.gameFormExtras(): List<Registered<UiElement.ElementWithChildren, UiElementRenderer>> =
    listOf(
        Label.expression()
    )

@Composable
public fun UiElement.Control.Companion.gameFormExtras(): List<Registered<UiElement.Control, ControlRenderer>> = listOf(
    ArrayControl.playerTable(),
    IntegerControl.tableControl(),
    StringControl.tableControl(),
    BooleanControl.tableControl(),

    IntegerControl.tableDropdown(),
)

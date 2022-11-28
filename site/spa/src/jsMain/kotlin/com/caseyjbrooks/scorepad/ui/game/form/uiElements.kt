package com.caseyjbrooks.scorepad.ui.game.form

import com.copperleaf.forms.compose.elements.UiElementRenderer
import com.copperleaf.forms.compose.form.Registered
import com.copperleaf.forms.compose.form.uiElement
import com.copperleaf.forms.core.Label
import com.copperleaf.forms.core.ui.UiElement
import com.copperleaf.json.values.optional
import com.copperleaf.json.values.string

public fun Label.expression(): Registered<UiElement.ElementWithChildren, UiElementRenderer> = uiElement(
    tester = { uiSchemaConfig.optional { string("expression") } != null },
    rank = 100,
) {
    val expression = this.uiSchema.json.string("expression")



}

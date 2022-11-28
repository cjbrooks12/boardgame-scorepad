package com.caseyjbrooks.scorepad.utils.form

//@Composable
//fun ScorepadForm(
//    definition: FormDefinition,
//    data: JsonElement,
//    onDataChanged: (JsonElement)->Unit,
//) {
//    val onDataChangedCallback by rememberUpdatedState(onDataChanged)
//
//    val injector = LocalInjector.current
//    val coroutineScope = rememberCoroutineScope()
//
//    val basicDataStore = remember(definition, onDataChangedCallback) {
//        BasicDataStore(
//            definition,
//            data,
//            onDataChangedCallback,
//        )
//    }
//
//    val vm = remember(coroutineScope, injector, basicDataStore) {
//        BasicFormViewModel(
//            coroutineScope,
//            FormSavedStateAdapter(basicDataStore) {
//                FormContract.State(
//                    saveType = FormContract.SaveType.OnAnyChange,
//                    validationMode = FormContract.ValidationMode.NoValidation,
//                    debug = false,
//                )
//            }
//        )
//    }
//
//    BulmaForm(vm)
//}

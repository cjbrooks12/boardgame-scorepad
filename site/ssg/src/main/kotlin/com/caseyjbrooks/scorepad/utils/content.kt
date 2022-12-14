package com.caseyjbrooks.scorepad.utils

import com.caseyjbrooks.scorepad.site.BuildConfig
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension
import com.vladsch.flexmark.ext.tables.TablesExtension
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.data.MutableDataSet
import kotlinx.datetime.Clock

private val replacements = listOf(
    "baseUrl" to BuildConfig.BASE_URL,
    "now" to Clock.System.now().toEpochMilliseconds().toString(),
)

fun getOutputExtension(extension: String): String {
    return when (extension) {
        "md" -> "html"
        else -> extension
    }
}

fun processByExtension(originalText: String, extension: String): String {
    val preprocessedText = originalText.preprocessContent()
    return when (extension) {
        "md" -> processMarkdown(preprocessedText)
        else -> processText(preprocessedText)
    }
}

fun String.preprocessContent(): String {
    return replacements
        .fold(this) { acc, (key, value) ->
            acc.replace("{{$key}}", value)
        }
}

private fun processText(inputText: String): String {
    return inputText
}

private fun processMarkdown(inputText: String): String {
    val options = MutableDataSet().apply {
        set(Parser.EXTENSIONS, listOf(TablesExtension.create(), StrikethroughExtension.create()));
    }

    return HtmlRenderer
        .builder(options)
        .build()
        .render(
            Parser
                .builder(options)
                .build()
                .parse(inputText)
        )
}

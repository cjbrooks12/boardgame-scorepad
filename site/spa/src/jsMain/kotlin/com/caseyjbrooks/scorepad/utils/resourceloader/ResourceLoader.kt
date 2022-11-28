package com.caseyjbrooks.scorepad.utils.resourceloader

interface ResourceLoader {
    suspend fun load(url: String): String
}

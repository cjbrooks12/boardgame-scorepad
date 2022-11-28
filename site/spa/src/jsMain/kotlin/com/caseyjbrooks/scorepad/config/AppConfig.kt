package com.caseyjbrooks.scorepad.config

interface AppConfig {
    val isPwa: Boolean
    val imageCacheSize: Int
    val debug: Boolean
    val useHistoryApi: Boolean
    val baseUrl: String
    val basePath: String?
}

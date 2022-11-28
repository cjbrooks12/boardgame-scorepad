package com.caseyjbrooks.scorepad.config

import com.caseyjbrooks.scorepad.app.BuildConfig

class AppConfigImpl(
    override val isPwa: Boolean,
    override val imageCacheSize: Int = 10,
    override val debug: Boolean = BuildConfig.DEBUG,
    override val useHistoryApi: Boolean = !BuildConfig.DEBUG,
    override val baseUrl: String = BuildConfig.BASE_URL,
    override val basePath: String? = BuildConfig.BASE_PATH,
) : AppConfig

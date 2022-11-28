package com.caseyjbrooks.scorepad.utils

import java.nio.file.Path
import kotlin.io.path.div

class SiteConfiguration(
    val rootDir: Path,

    val contentDir: Path = rootDir / "content",
    val assetsDir: Path = contentDir / "assets",
    val staticDir: Path = contentDir / "static",
    val pagesDir: Path = contentDir / "pages",
    val schemasDir: Path = contentDir / "schemas",

    val outputDir: Path = rootDir / "build/distributions",
    val hashesDir: Path = rootDir / "build/cache/site",
    val httpCacheDir: Path = rootDir / "build/cache/http",
)

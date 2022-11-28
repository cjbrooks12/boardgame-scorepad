package com.caseyjbrooks.scorepad

import com.caseyjbrooks.scorepad.dag.DependencyGraph
import com.caseyjbrooks.scorepad.dag.renderer.LocalServerRenderer
import com.caseyjbrooks.scorepad.dag.renderer.StaticOutputRenderer
import com.caseyjbrooks.scorepad.site.BuildConfig
import com.caseyjbrooks.scorepad.stages.assets.CopyOtherAssets
import com.caseyjbrooks.scorepad.stages.assets.RasterizeSvgs
import com.caseyjbrooks.scorepad.stages.config.ConfigStage
import com.caseyjbrooks.scorepad.stages.content.StaticContent
import com.caseyjbrooks.scorepad.stages.content.StaticPages
import com.caseyjbrooks.scorepad.stages.copyscripts.CopyScripts
import com.caseyjbrooks.scorepad.stages.games.GameSchemas
import com.caseyjbrooks.scorepad.stages.meta.RobotsTxt
import com.caseyjbrooks.scorepad.stages.meta.Sitemap
import com.caseyjbrooks.scorepad.utils.SiteConfiguration
import kotlinx.coroutines.runBlocking
import kotlin.io.path.Path
import kotlin.io.path.absolute

fun main(): Unit = runBlocking {
    System.setProperty("jsava.awt.headless", "true")

    val graph = DependencyGraph(
        config = SiteConfiguration(
            rootDir = Path(System.getenv("GITHUB_WORKSPACE") ?: "./../../").absolute().normalize(),
        ),

        ConfigStage(1),
//        Favicons(),
        RobotsTxt(),
        Sitemap(),
        StaticContent(),
        StaticPages(),
        RasterizeSvgs(),
        CopyOtherAssets(),
        CopyScripts(),
        GameSchemas(),

        renderers = buildList {
            if (BuildConfig.DEBUG) {
                this += LocalServerRenderer(8080)
            }
            this += StaticOutputRenderer()
        },
//        updater = if (BuildConfig.DEBUG) {
//            ConstantUpdaterService(10.seconds)
//        } else {
//            ImmediateUpdaterService()
//        },
    )

    graph.executeGraph()
}

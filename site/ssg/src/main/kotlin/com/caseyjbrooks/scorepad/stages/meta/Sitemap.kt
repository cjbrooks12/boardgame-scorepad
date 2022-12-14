package com.caseyjbrooks.scorepad.stages.meta

import com.caseyjbrooks.scorepad.dag.DependencyGraphBuilder
import com.caseyjbrooks.scorepad.dag.path.TerminalPathNode
import com.caseyjbrooks.scorepad.site.BuildConfig
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import java.nio.file.Paths

@Suppress("BlockingMethodInNonBlockingContext")
class Sitemap : DependencyGraphBuilder {

    override suspend fun DependencyGraphBuilder.Scope.buildGraph() {
        if (graph.currentIteration == 1) {
            addNode(
                TerminalPathNode(
                    baseOutputDir = graph.config.outputDir,
                    outputPath = Paths.get("sitemap.xml"),
                    doRender = { _, os ->
                        os.write(
                            """|
                            |<sitemapindex 
                            |    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
                            |    xmlns="http://www.sitemaps.org/schemas/sitemap/0.9" 
                            |    xsi:schemaLocation="http://www.sitemaps.org/schemas/sitemap/0.9 http://www.sitemaps.org/schemas/sitemap/0.9/siteindex.xsd"
                            |    >
                            |    <sitemap>
                            |        <loc>${BuildConfig.BASE_URL}/</loc>
                            |        <lastmod>${Clock.System.todayIn(TimeZone.currentSystemDefault())}</lastmod>
                            |    </sitemap>
                            |</sitemapindex>
                        """.trimMargin().toByteArray()
                        )
                    }
                )
            )
        }
    }
}

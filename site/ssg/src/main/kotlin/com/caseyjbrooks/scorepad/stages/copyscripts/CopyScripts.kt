package com.caseyjbrooks.scorepad.stages.copyscripts

import com.caseyjbrooks.scorepad.dag.DependencyGraphBuilder
import com.caseyjbrooks.scorepad.dag.path.InputPathNode
import com.caseyjbrooks.scorepad.dag.path.StartPathNode
import com.caseyjbrooks.scorepad.dag.path.TerminalPathNode
import com.caseyjbrooks.scorepad.site.BuildConfig
import com.caseyjbrooks.scorepad.stages.config.SiteConfigNode
import com.caseyjbrooks.scorepad.utils.destruct1
import kotlin.io.path.div
import kotlin.io.path.name
import kotlin.io.path.readText

@Suppress("BlockingMethodInNonBlockingContext")
class CopyScripts : DependencyGraphBuilder {

    private var startIteration = Int.MIN_VALUE
    override suspend fun DependencyGraphBuilder.Scope.buildGraph() {
        if (graph.containsNode { it is SiteConfigNode } && startIteration == Int.MIN_VALUE) {
            startIteration = graph.currentIteration
        }

        when (graph.currentIteration) {
            startIteration -> loadInputScripts()
            startIteration + 1 -> createOutputScripts()
        }
    }

    private suspend fun DependencyGraphBuilder.Scope.loadInputScripts() {
        val siteConfigNode = graph.getNodeOfType<SiteConfigNode>()
        val spaDistributionDir = if(BuildConfig.DEBUG) {
            graph.config.rootDir / "site/spa/build/developmentExecutable"
        } else {
            graph.config.rootDir / "site/spa/build/distributions"
        }

        graph
            .resourceService
            .getFilesInDir(spaDistributionDir)
            .filterNot { it.name == "index.html" }
            .forEach { inputPath ->
                addNodeAndEdge(
                    start = siteConfigNode,
                    newEndNode = StartPathNode(
                        tags = listOf("CopyScripts", "input"),
                        baseInputDir = spaDistributionDir,
                        inputPath = inputPath,
                    )
                )
            }
    }

    private suspend fun DependencyGraphBuilder.Scope.createOutputScripts() {
        graph
            .getNodesOfType<InputPathNode> { it.meta.tags == listOf("CopyScripts", "input") }
            .forEach { inputNodeQuery ->
                val relativeOutputPath = inputNodeQuery.inputPath
                addNodeAndEdge(
                    start = inputNodeQuery,
                    newEndNode = TerminalPathNode(
                        tags = listOf("CopyScripts", "output"),
                        baseOutputDir = graph.config.outputDir,
                        outputPath = relativeOutputPath,
                        doRender = { inputNodes, os ->
                            val (sourceContentFile) = inputNodes.destruct1<InputPathNode>()
                            os.write(
                                sourceContentFile
                                    .realInputFile()
                                    .readText()
                                    .replace("sourceMappingURL=spa.js.map", "sourceMappingURL=${BuildConfig.BASE_URL}/spa.js.map")
                                    .toByteArray()
                            )
                        },
                    ),
                )
            }
    }
}

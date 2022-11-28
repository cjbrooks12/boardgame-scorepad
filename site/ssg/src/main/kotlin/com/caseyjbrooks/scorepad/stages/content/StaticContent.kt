package com.caseyjbrooks.scorepad.stages.content

import com.caseyjbrooks.scorepad.dag.DependencyGraphBuilder
import com.caseyjbrooks.scorepad.dag.path.InputPathNode
import com.caseyjbrooks.scorepad.dag.path.StartPathNode
import com.caseyjbrooks.scorepad.dag.path.TerminalPathNode
import com.caseyjbrooks.scorepad.stages.config.SiteConfigNode
import com.caseyjbrooks.scorepad.utils.destruct1
import com.caseyjbrooks.scorepad.utils.getOutputExtension
import com.caseyjbrooks.scorepad.utils.processByExtension
import com.caseyjbrooks.scorepad.utils.withExtension
import kotlin.io.path.extension
import kotlin.io.path.readText

class StaticContent : DependencyGraphBuilder {
    private var startIteration = Int.MIN_VALUE
    override suspend fun DependencyGraphBuilder.Scope.buildGraph() {
        if (graph.containsNode { it is SiteConfigNode } && startIteration == Int.MIN_VALUE) {
            startIteration = graph.currentIteration
        }

        when (graph.currentIteration) {
            startIteration -> loadInputContentFiles()
            startIteration + 1 -> createOutputFiles()
        }
    }

    private suspend fun DependencyGraphBuilder.Scope.loadInputContentFiles() {
        val siteConfigNode = graph.getNodeOfType<SiteConfigNode>()
        graph
            .resourceService
            .getFilesInDirs(graph.config.staticDir)
            .forEach { inputPath ->
                addNodeAndEdge(
                    start = siteConfigNode,
                    newEndNode = StartPathNode(
                        baseInputDir = graph.config.staticDir,
                        inputPath = inputPath,
                        tags = listOf("StaticContent", "input"),
                    )
                )
            }
    }

    private suspend fun DependencyGraphBuilder.Scope.createOutputFiles() {
        graph
            .getNodesOfType<InputPathNode> {
                it.meta.tags == listOf("StaticContent", "input")
            }
            .forEach { inputNodeQuery ->
                val outputPath = inputNodeQuery.inputPath.withExtension(
                    getOutputExtension(inputNodeQuery.inputPath.extension)
                )
                addNodeAndEdge(
                    start = inputNodeQuery,
                    newEndNode = TerminalPathNode(
                        baseOutputDir = graph.config.outputDir,
                        outputPath = outputPath,
                        tags = listOf("StaticContent", "output"),
                        doRender = { inputNodes, os ->
                            val (sourceContentFile) = inputNodes.destruct1<InputPathNode>()
                            val sourceText = sourceContentFile.realInputFile().readText()
                            val outputText = processByExtension(sourceText, sourceContentFile.inputPath.extension)
                            os.write(outputText.toByteArray())
                        },
                    ),
                )
            }
    }
}

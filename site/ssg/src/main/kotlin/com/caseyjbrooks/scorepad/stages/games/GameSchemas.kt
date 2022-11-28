package com.caseyjbrooks.scorepad.stages.games

import com.caseyjbrooks.scorepad.dag.DependencyGraphBuilder
import com.caseyjbrooks.scorepad.dag.http.prettyJson
import com.caseyjbrooks.scorepad.dag.path.InputPathNode
import com.caseyjbrooks.scorepad.dag.path.StartPathNode
import com.caseyjbrooks.scorepad.dag.path.TerminalPathNode
import com.caseyjbrooks.scorepad.stages.config.SiteConfigNode
import com.caseyjbrooks.scorepad.utils.destruct1
import com.copperleaf.scorepad.models.api.GameId
import com.copperleaf.scorepad.models.api.GameType
import com.copperleaf.scorepad.models.api.GameTypeList
import com.copperleaf.scorepad.models.api.toLiteObject
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import kotlin.io.path.Path
import kotlin.io.path.div
import kotlin.io.path.inputStream
import kotlin.io.path.name
import kotlin.io.path.readBytes

@Suppress("BlockingMethodInNonBlockingContext")
class GameSchemas : DependencyGraphBuilder {
    private var startIteration = Int.MIN_VALUE
    override suspend fun DependencyGraphBuilder.Scope.buildGraph() {
        if (graph.containsNode { it is SiteConfigNode } && startIteration == Int.MIN_VALUE) {
            startIteration = graph.currentIteration
        }

        when (graph.currentIteration) {
            startIteration -> loadInputImages()
            startIteration + 1 -> createOutputFiles()
        }
    }

    private suspend fun DependencyGraphBuilder.Scope.loadInputImages() {
        val siteConfigNode = graph.getNodeOfType<SiteConfigNode>()
        graph
            .resourceService
            .getFilesInDirs(graph.config.schemasDir)
            .forEach { inputPath ->
                addNodeAndEdge(
                    start = siteConfigNode,
                    newEndNode = StartPathNode(
                        tags = listOf("GameSchemas", "input"),
                        baseInputDir = graph.config.schemasDir,
                        inputPath = inputPath,
                    )
                )
            }
    }

    private suspend fun DependencyGraphBuilder.Scope.createOutputFiles() {
        graph
            .getNodesOfType<InputPathNode> { it.meta.tags == listOf("GameSchemas", "input") }
            .forEach { inputNode ->
                // copy the file over directly
                val outputPath = inputNode.inputPath
                addNodeAndEdge(
                    start = inputNode,
                    newEndNode = TerminalPathNode(
                        tags = listOf("GameSchemas", "output"),
                        baseOutputDir = graph.config.outputDir / "api/schemas",
                        outputPath = outputPath,
                        doRender = { inputNodes, os ->
                            val (sourceFile) = inputNodes.destruct1<InputPathNode>()
                            os.write(sourceFile.realInputFile().readBytes())
                        },
                    ),
                )
            }

        val outputNode = addNode(
            TerminalPathNode(
                baseOutputDir = graph.config.outputDir / "api",
                outputPath = Path("games.json"),
                tags = listOf("GameSchemas", "output"),
                doRender = { inputNodes, os ->
                    val inputs = inputNodes
                        .filterIsInstance<InputPathNode>()
                        .map { inputNode ->
                            val gameId = inputNode.realInputFile().parent.name
                            prettyJson.decodeFromStream<GameType>(
                                inputNode.realInputFile().inputStream()
                            ).toLiteObject(GameId(gameId))
                        }

                    prettyJson.encodeToStream(GameTypeList.serializer(), GameTypeList(inputs), os)
                },
            ),
        )

        graph
            .getNodesOfType<InputPathNode> {
                it.meta.tags == listOf("GameSchemas", "input") && it.inputPath.fileName == Path("info.json")
            }
            .forEach { inputNodeQuery ->
                addEdge(inputNodeQuery, outputNode)
            }
    }
}

package com.caseyjbrooks.scorepad.stages.config

import com.caseyjbrooks.scorepad.dag.DependencyGraphBuilder

class ConfigStage(val version: Int) : DependencyGraphBuilder {
    override suspend fun DependencyGraphBuilder.Scope.buildGraph() {
        when (graph.currentIteration) {
            1 -> {
                addNode(
                    SiteConfigNode(version)
                )
            }
        }
    }
}

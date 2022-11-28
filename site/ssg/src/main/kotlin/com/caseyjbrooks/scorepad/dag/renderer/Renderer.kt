package com.caseyjbrooks.scorepad.dag.renderer

import com.caseyjbrooks.scorepad.dag.DependencyGraph
import com.caseyjbrooks.scorepad.dag.Node

/**
 * Renders the collected Inputs and Outputs.
 */
interface Renderer {
    suspend fun start(graph: DependencyGraph)
    suspend fun renderDirtyOutputs(graph: DependencyGraph, dirtyOutputs: List<Node.Output>)
}

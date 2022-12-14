package com.caseyjbrooks.scorepad.stages.config

import com.caseyjbrooks.scorepad.dag.DependencyGraph
import com.caseyjbrooks.scorepad.dag.Node
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile
import kotlin.io.path.div
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.io.path.writeText

@Suppress("RedundantIf")
data class SiteConfigNode(
    val version: Int,
) : Node.Input {
    override val meta: Node.Meta = Node.Meta(
        name = "SiteVersion($version)",
        tags = listOf("ConfigStage", "v$version")
    )

    override suspend fun preload(graph: DependencyGraph) {
        // nothing to preload
    }

    override suspend fun dirty(graph: DependencyGraph): Boolean {
        val versionFile = graph.config.hashesDir / "siteVersion.txt"
        return if (versionFile.exists()) {
            // we have a site version file, check what version the previous render was in
            if (versionFile.readText().trim().toInt() != version) {
                true
            } else {
                false
            }
        } else {
            // we have not written a site version, mark it as dirty
            true
        }
    }

    override suspend fun markClean(graph: DependencyGraph) {
        (graph.config.hashesDir / "siteVersion.txt").apply {
            parent.createDirectories()
            if(!exists()) {
                createFile()
            }
            writeText(version.toString())
        }
    }

    override fun toString(): String {
        return "SiteConfigNode(${version})"
    }
}

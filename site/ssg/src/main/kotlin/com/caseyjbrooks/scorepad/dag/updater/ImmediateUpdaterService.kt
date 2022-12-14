package com.caseyjbrooks.scorepad.dag.updater

import com.caseyjbrooks.scorepad.dag.DependencyGraph
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * Emit a single change to the dependency graph, so it is only ever built once.
 */
class ImmediateUpdaterService : UpdaterService {

    override fun watchForChanges(graph: DependencyGraph): Flow<Unit> = flowOf(Unit)

}

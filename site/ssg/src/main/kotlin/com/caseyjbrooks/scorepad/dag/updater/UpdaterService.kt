package com.caseyjbrooks.scorepad.dag.updater

import com.caseyjbrooks.scorepad.dag.DependencyGraph
import kotlinx.coroutines.flow.Flow

interface UpdaterService {

    fun watchForChanges(graph: DependencyGraph): Flow<Unit>

}

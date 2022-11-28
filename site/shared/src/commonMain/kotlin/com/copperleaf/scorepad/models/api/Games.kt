package com.copperleaf.scorepad.models.api

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class GameId(val id: String) : Comparable<GameId> {
    override fun compareTo(other: GameId): Int {
        return this.id.compareTo(other.id)
    }
}

@Serializable
data class GameType(
    val name: String,
    val bggId: Int,
)

@Serializable
data class GameTypeLite(
    val id: GameId,
    val name: String,
)

@Serializable
data class GameTypeList(
    val gameTypes: List<GameTypeLite>
)

fun GameType.toLiteObject(id: GameId): GameTypeLite {
    return GameTypeLite(
        id = id,
        name = name,
    )
}

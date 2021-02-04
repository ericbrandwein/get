package gamelogic

import Player
import gamelogic.map.Continent

interface SubGoal {
    fun achieved(playerInfo: PlayerInfo, gameInfo: GameInfo): Boolean
}

class Goal(private val subGoals: Collection<SubGoal>) : SubGoal {
    override fun achieved(playerInfo: PlayerInfo, gameInfo: GameInfo): Boolean {
        return subGoals.all { it.achieved(playerInfo, gameInfo) }
    }
}

class OccupyContinent(private val continent: Continent) :
    SubGoal by OccupySubContinent(continent, continent.countries.size)

class OccupySubContinent(
    private val continent: Continent, private val countries: Int
) : SubGoal {
    init {
        if (continent.countries.size < countries) {
            throw Exception("${continent.name} has less than $countries countries")
        }
    }

    override fun achieved(playerInfo: PlayerInfo, gameInfo: GameInfo): Boolean =
        countriesOccupiedBy(playerInfo, gameInfo) >= countries

    private fun countriesOccupiedBy(playerInfo: PlayerInfo, gameInfo: GameInfo) =
        continent.countries.count {
            gameInfo.occupations.occupierOf(it) == playerInfo.name
        }
}

class Destroy(private val playerToDestroy: Player) : SubGoal {
    override fun achieved(playerInfo: PlayerInfo, gameInfo: GameInfo): Boolean {
        val destroyedPlayers = gameInfo.destroyedPlayers
        return destroyedPlayers.isDestroyed(playerToDestroy) &&
            destroyedPlayers.destroyerOf(playerToDestroy) == playerInfo.name
    }
}

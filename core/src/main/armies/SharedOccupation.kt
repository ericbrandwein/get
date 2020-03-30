package armies

import Player

class SharedOccupation(
    firstPlayer: Player, firstArmies: Int, secondPlayer: Player, secondArmies: Int
) : Occupation {

    private val occupations =
        mutableMapOf(
            firstPlayer to SinglePlayerOccupation(firstPlayer, firstArmies),
            secondPlayer to SinglePlayerOccupation(secondPlayer, secondArmies)
        ).withDefault { player -> throw NotOccupyingPlayerException(player) }

    val occupiers = occupations.keys

    init {
        assertDifferentPlayersOccupying(firstPlayer, secondPlayer)
    }

    private fun assertDifferentPlayersOccupying(
        firstPlayer: Player, secondPlayer: Player) {
        if (firstPlayer == secondPlayer) {
            throw CantShareCountryWithYourselfException(firstPlayer)
        }
    }

    override fun isShared() = true

    override fun occupy(occupier: Player, armies: Int): SinglePlayerOccupation {
        assertPlayerIsNotOccupying(occupier)
        return super.occupy(occupier, armies)
    }

    override fun occupy(
        firstPlayer: Player, firstArmies: Int, secondPlayer: Player,
        secondArmies: Int
    ): SharedOccupation {
        assertPlayerIsNotOccupying(firstPlayer)
        assertPlayerIsNotOccupying(secondPlayer)
        return super.occupy(firstPlayer, firstArmies, secondPlayer, secondArmies)
    }

    private fun assertPlayerIsNotOccupying(player: Player) {
        if (player in occupiers) {
            throw PlayerAlreadyOccupiesCountryException(player)
        }
    }

    fun armiesOf(player: Player) = occupations.getValue(player).armies

    fun addArmies(added: Int, player: Player) =
        occupations.getValue(player).addArmies(added)

    fun removeArmies(removed: Int, player: Player) =
        occupations.getValue(player).removeArmies(removed)

    fun removePlayer(player: Player): SinglePlayerOccupation {
        assertPlayerIsOccupying(player)
        val otherPlayer = occupiers.first { it != player }
        return occupations.getValue(otherPlayer)
    }

    private fun assertPlayerIsOccupying(player: Player) {
        if (player !in occupiers) {
            throw NotOccupyingPlayerException(player)
        }
    }
}

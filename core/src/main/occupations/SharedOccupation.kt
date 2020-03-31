package occupations

import Player

/**
 * An Occupation where there are two players occupying the same country.
 */
class SharedOccupation(
    firstPlayer: Player, firstArmies: Int, secondPlayer: Player, secondArmies: Int
) : Occupation {

    private val occupations =
        mutableMapOf(
            firstPlayer to SinglePlayerOccupation(firstPlayer, firstArmies),
            secondPlayer to SinglePlayerOccupation(secondPlayer, secondArmies)
        ).withDefault { player -> throw NotOccupyingPlayerException(player) }

    val occupiers get() = occupations.keys

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

    fun replacePlayer(oldPlayer: Player, newPlayer: Player, armies: Int) {
        assertPlayerIsOccupying(oldPlayer)
        assertPlayerIsNotOccupying(newPlayer)
        occupations.remove(oldPlayer)
        addPlayer(newPlayer, armies)
    }

    private fun assertPlayerIsNotOccupying(player: Player) {
        if (player in occupiers) {
            throw PlayerAlreadyOccupiesCountryException(player)
        }
    }

    private fun addPlayer(player: Player, armies: Int) {
        occupations[player] = SinglePlayerOccupation(player, armies)
    }

    override fun accept(visitor: OccupationVisitor) = visitor.visit(this)
}

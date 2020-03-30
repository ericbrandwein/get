package armies

import Player

class SharedOccupation(
    firstPlayer: Player, firstArmies: Int, secondPlayer: Player, secondArmies: Int
) : Occupation {

    init {
        assertDifferentPlayersOccupying(firstPlayer, secondPlayer)
        assertPositiveArmies(firstArmies)
        assertPositiveArmies(secondArmies)
    }

    private val armiesForOccupiers =
        mutableMapOf(firstPlayer to firstArmies, secondPlayer to secondArmies)
            .withDefault { player -> throw NotOccupyingPlayerException(player) }

    val occupiers = armiesForOccupiers.keys

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

    fun armiesOf(player: Player) = armiesForOccupiers.getValue(player)

    private fun assertDifferentPlayersOccupying(
        firstPlayer: Player, secondPlayer: Player) {
        if (firstPlayer == secondPlayer) {
            throw CantShareCountryWithYourselfException(firstPlayer)
        }
    }

    private fun assertPositiveArmies(armies: Int) {
        if (armies <= 0) {
            throw NonPositiveArmiesException(armies)
        }
    }

    fun addArmies(added: Int, player: Player) {
        assertPositiveAmountAdded(added)
        armiesForOccupiers[player] = armiesForOccupiers.getValue(player) + added
    }

    private fun assertPositiveAmountAdded(added: Int) {
        if (added <= 0) {
            throw NonPositiveArmiesAddedException(added)
        }
    }
}

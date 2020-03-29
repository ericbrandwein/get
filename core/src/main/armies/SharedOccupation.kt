package armies

import Country

class SharedOccupation(
    override val country: Country,
    firstPlayer: Player,
    secondPlayer: Player,
    firstArmies: Int,
    secondArmies: Int
) : Occupation {

    private val armiesForPlayers =
        mutableMapOf(firstPlayer to firstArmies, secondPlayer to secondArmies)
            .withDefault { throw PlayerIsNotOccupyingCountryException(country, it) }

    override val armies get() = armiesForPlayers.values.sum()
    override val occupiers = setOf(firstPlayer, secondPlayer)
    override val occupier get() = throw NoSingleOccupierException(country)

    override fun isShared() = true

    override fun addArmies(armies: Int) = throw NoSingleOccupierException(country)

    override fun addArmies(armies: Int, player: Player) {
        armiesForPlayers[player] = armiesForPlayers.getValue(player) + armies
    }

    override fun removeArmies(armies: Int) = throw NoSingleOccupierException(country)

    override fun removeArmies(armies: Int, player: Player) {
        assertPlayerCanRemove(player, armies)
        armiesForPlayers[player] = armiesForPlayers.getValue(player) - armies
    }

    private fun assertPlayerCanRemove(player: Player, armies: Int) {
        val playerArmies = armiesForPlayers.getValue(player)
        if (playerArmies <= armies) {
            throw CantRemoveMoreArmiesThanAvailableException(
                country, playerArmies, armies)
        }
    }

    override fun take(
        firstPlayer: Player, secondPlayer: Player, firstArmies: Int,
        secondArmies: Int
    ): Occupation {
        assertIsNotOccupiedBy(firstPlayer)
        assertIsNotOccupiedBy(secondPlayer)
        return super.take(firstPlayer, secondPlayer, firstArmies, secondArmies)
    }

    override fun takeShare(
        currentPlayer: Player, newPlayer: Player, armies: Int): Occupation {
        assertIsOccupiedBy(currentPlayer)
        assertIsNotOccupiedBy(newPlayer)
        val keptPlayer = armiesForPlayers.keys.first { it != currentPlayer }
        return SharedOccupation(
            country, newPlayer, keptPlayer, armies, armiesForPlayers.getValue(keptPlayer))
    }

    private fun assertIsOccupiedBy(currentPlayer: Player) {
        if (!occupiers.contains(currentPlayer)) {
            throw PlayerIsNotOccupyingCountryException(country, currentPlayer)
        }
    }

    override fun armiesFor(player: Player) = armiesForPlayers.getValue(player)

    private fun assertIsNotOccupiedBy(player: Player) {
        if (player in occupiers) {
            throw CountryAlreadyOccupiedByPlayerException(country, player)
        }
    }
}

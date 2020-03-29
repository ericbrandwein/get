package armies

import Country

/**
 * An Occupation with only one Player occupying the country.
 *
 * One can't take a share of a country if it is being occupied by just one Player.
 */
class SinglePlayerOccupation(
    override val country: Country,
    override val occupier: Player,
    private var internalArmies: Int
) : Occupation {

    override val occupiers = setOf(occupier)
    override val armies get() = internalArmies

    override fun armiesFor(player: Player): Int {
        assertIsOccupiedBy(player)
        return armies
    }

    private fun assertIsOccupiedBy(player: Player) {
        if (player != occupier) {
            throw PlayerIsNotOccupyingCountryException(country, player)
        }
    }

    override fun take(player: Player, armies: Int): Occupation {
        assertCanTake(player)
        return super.take(player, armies)
    }

    override fun take(
        firstPlayer: Player, secondPlayer: Player, firstArmies: Int,
        secondArmies: Int
    ): Occupation {
        assertCanTake(firstPlayer)
        assertCanTake(secondPlayer)
        return super.take(firstPlayer, secondPlayer, firstArmies, secondArmies)
    }

    private fun assertCanTake(player: Player) {
        if (player == occupier) {
            throw CountryAlreadyOccupiedByPlayerException(country, player)
        }
    }

    override fun addArmies(armies: Int, player: Player) {
        assertIsOccupiedBy(player)
        addArmies(armies)
    }

    override fun addArmies(armies: Int) {
        internalArmies += armies
    }

    override fun removeArmies(armies: Int, player: Player) {
        assertIsOccupiedBy(player)
        removeArmies(armies)
    }

    override fun takeShare(currentPlayer: Player, newPlayer: Player, armies: Int) =
        throw CountryNotSharedException(country)

    override fun removeArmies(armies: Int) {
        assertCanRemove(armies)
        internalArmies -= armies
    }

    private fun assertCanRemove(armies: Int) {
        if (armies >= this.armies) {
            throw CantRemoveMoreArmiesThanAvailableException(country, this.armies, armies)
        }
    }
}

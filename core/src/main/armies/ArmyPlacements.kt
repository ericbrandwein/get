package armies

import Country

typealias Player = String

class ArmyPlacements {

    private val occupiedCountries =
        mutableMapOf<Country, Occupation>()
            .withDefault { country -> NoOccupation(country) }

    fun takeCountry(country: Country, player: Player, armies: Int) {
        assertTakingWithPositiveAmount(armies)
        occupiedCountries[country] = occupationForCountry(country).take(player, armies)
    }

    fun isOccupied(country: Country) = occupiedCountries.containsKey(country)

    fun occupierFor(country: Country) = occupationForCountry(country).occupier

    fun armiesIn(country: Country) = occupationForCountry(country).armies

    fun addArmies(country: Country, armies: Int) {
        assertPositiveAmountAdded(armies)
        occupationForCountry(country).addArmies(armies)
    }

    fun removeArmies(country: Country, amount: Int) {
        assertPositiveAmountRemoved(amount)
        occupationForCountry(country).removeArmies(amount)
    }

    private fun assertTakingWithPositiveAmount(armies: Int) {
        if (armies <= 0) {
            throw CantOccupyWithNoArmiesException(armies)
        }
    }

    private fun assertPositiveAmountAdded(amount: Int) {
        if (amount <= 0) {
            throw NonPositiveAmountAddedException(amount)
        }
    }

    private fun assertPositiveAmountRemoved(amount: Int) {
        if (amount <= 0) {
            throw NonPositiveAmountRemovedException(amount)
        }
    }

    private fun occupationForCountry(country: Country) =
        occupiedCountries.getValue(country)
}

interface Occupation {
    val country: Country
    val occupier: Player
    val armies: Int

    fun take(player: Player, armies: Int): Occupation =
        SinglePlayerOccupation(country, player, armies)

    fun addArmies(armies: Int)
    fun removeArmies(armies: Int)
}

class NoOccupation(override val country: Country) : Occupation {
    override val occupier get() = throw UnoccupiedCountryException(country)
    override val armies = 0

    override fun addArmies(armies: Int) = throw UnoccupiedCountryException(country)
    override fun removeArmies(armies: Int) = throw UnoccupiedCountryException(country)
}

class SinglePlayerOccupation(
    override val country: Country,
    override val occupier: Player,
    private var internalArmies: Int
) : Occupation {

    override val armies get() = internalArmies

    override fun take(player: Player, armies: Int): Occupation {
        assertCanTake(player)
        return super.take(player, armies)
    }

    private fun assertCanTake(player: Player) {
        if (player == occupier) {
            throw CountryAlreadyOccupiedByPlayerException(country, player)
        }
    }

    override fun addArmies(armies: Int) {
        internalArmies += armies
    }

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

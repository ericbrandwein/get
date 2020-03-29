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

    fun takeCountry(
        country: Country, firstPlayer: Player, secondPlayer: Player,
        firstArmies: Int, secondArmies: Int
    ) {
        assertTakingWithPositiveAmount(firstArmies)
        assertTakingWithPositiveAmount(secondArmies)
        occupiedCountries[country] =
            occupationForCountry(country)
                .take(firstPlayer, secondPlayer, firstArmies, secondArmies)
    }

    fun takeShare(
        country: Country, currentPlayer: Player, newPlayer: Player, armies: Int) {
        assertTakingWithPositiveAmount(armies)
        occupiedCountries[country] =
            occupationForCountry(country).takeShare(currentPlayer, newPlayer, armies)
    }

    fun isOccupied(country: Country) = occupiedCountries.containsKey(country)

    fun isShared(country: Country) = occupationForCountry(country).isShared()

    fun occupierFor(country: Country) = occupationForCountry(country).occupier

    fun occupiersFor(country: Country) = occupationForCountry(country).occupiers

    fun armiesIn(country: Country) = occupationForCountry(country).armies

    fun armiesIn(country: Country, player: Player) =
        occupationForCountry(country).armiesFor(player)

    fun addArmies(country: Country, armies: Int) {
        assertPositiveAmountAdded(armies)
        occupationForCountry(country).addArmies(armies)
    }

    fun addArmies(country: Country, armies: Int, player: Player) {
        assertPositiveAmountAdded(armies)
        occupationForCountry(country).addArmies(armies, player)
    }

    fun removeArmies(country: Country, amount: Int) {
        assertPositiveAmountRemoved(amount)
        occupationForCountry(country).removeArmies(amount)
    }

    fun removeArmies(country: Country, amount: Int, player: Player) {
        assertPositiveAmountRemoved(amount)
        occupationForCountry(country).removeArmies(amount, player)
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


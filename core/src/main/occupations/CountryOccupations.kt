package occupations

import Country
import Player
import PositiveInt
import map.NonExistentCountryException

class CountryOccupations(occupations: Collection<Occupation>) {

    private val occupations = occupations.toMutableSet()

    fun occupierOf(country: Country) = occupationOf(country).occupier

    fun armiesOf(country: Country) = occupationOf(country).armies

    fun occupy(country: Country, player: Player, armies: PositiveInt) {
        removePreviousOccupation(country)
        addNewOccupation(country, player, armies)
    }

    private fun removePreviousOccupation(country: Country) {
        val previousOccupation = occupationOf(country)
        occupations.remove(previousOccupation)
    }

    private fun addNewOccupation(country: Country, player: Player, armies: PositiveInt) {
        val newOccupation = Occupation(country, player, armies)
        occupations.add(newOccupation)
    }

    fun addArmies(country: Country, armies: PositiveInt) =
        occupationOf(country).addArmies(armies)

    fun removeArmies(country: Country, armies: PositiveInt) =
        occupationOf(country).removeArmies(armies)

    private fun occupationOf(country: Country): Occupation {
        return occupations.find { it.country == country }
            ?: throw NonExistentCountryException(country)
    }
}

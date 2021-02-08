package gamelogic.occupations

import Country
import Player
import PositiveInt
import gamelogic.map.NonExistentCountryException

class CountryOccupations(occupations: Collection<Occupation>) {
    private val occupations: MutableMap<Country, Occupation> =
        occupations
            .associateBy { it.country }
            .toMutableMap()
            .withDefault { throw NonExistentCountryException(it) }

    fun isEmpty(country: Country) = occupationOf(country).isEmpty

    fun occupierOf(country: Country) = occupationOf(country).occupier

    fun armiesOf(country: Country): Int = occupationOf(country).armies

    fun addArmies(country: Country, armies: PositiveInt) =
        occupationOf(country).addArmies(armies)

    fun removeArmies(country: Country, armies: PositiveInt) {
        if (occupationOf(country).armies == armies.toInt()) {
            removeOccupation(country)
        } else {
            occupationOf(country).removeArmies(armies)
        }
    }

    fun occupy(country: Country, player: Player, armies: PositiveInt) {
        assertCountryIsNotOccupied(country)
        addOccupation(country, player, armies)
    }

    private fun removeOccupation(country: Country) {
        assertCountryExists(country)
        occupations[country] = NoOccupation(country)
    }

    private fun addOccupation(country: Country, player: Player, armies: PositiveInt) {
        occupations[country] = PlayerOccupation(country, player, armies)
    }

    fun assertCountryExists(country: Country) {
        if (country !in occupations) {
            throw NonExistentCountryException(country)
        }
    }

    private fun assertCountryIsNotOccupied(country: Country) {
        if (!occupationOf(country).isEmpty) {
            throw OccupiedCountryException(country)
        }
    }

    private fun occupationOf(country: Country) = occupations.getValue(country)
}

class OccupiedCountryException(val country: Country) :
    Exception(
        "Country $country is already occupied by another player. " +
            "Remove their armies before trying to occupy it.")

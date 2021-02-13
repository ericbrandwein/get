package gamelogic.combat

import Country
import PositiveInt
import gamelogic.occupations.CountryOccupations

class Occupier(private val occupations: CountryOccupations) {
    fun occupy(from: Country, to: Country, armies: PositiveInt) {
        occupations.assertCountryExists(from)
        occupations.assertCountryExists(to)
        assertCanMove(armies, from)
        occupations.removeArmies(from, armies)
        occupations.occupy(to, occupations.occupierOf(from), armies)
    }

    private fun assertCanMove(armies: PositiveInt, from: Country) {
        if (
            armies.toInt() >= occupations.armiesOf(from) ||
            armies > MAX_CONQUERING_ARMIES
        ) {
            throw TooManyArmiesMovedException(armies)
        }
    }

    companion object {
        val MAX_CONQUERING_ARMIES = PositiveInt(3)
    }
}

class TooManyArmiesMovedException(val armies: PositiveInt) : Exception(
    "Can't move $armies armies."
)

package occupations

import Continent
import Country
import Player
import assertCountryExists

class CountryOccupations(private val continents: Set<Continent>) {

    private val occupations =
        mutableMapOf<Country, Occupation>().withDefault { NoOccupation() }

    fun of(country: Country): Occupation {
        continents.assertCountryExists(country)
        return occupations.getValue(country)
    }

    fun occupy(country: Country, player: Player, armies: Int) {
        assertPlayerDoesNotOccupy(country, player)
        occupations[country] = SinglePlayerOccupation(player, armies)
    }

    private fun assertPlayerDoesNotOccupy(country: Country, player: Player) {
        val occupation = of(country)
        if (PlayerOccupationChecker(player).doesPlayerOccupy(occupation)) {
            throw PlayerAlreadyOccupiesCountryException(player)
        }
    }

    fun occupy(
        country: Country,
        firstPlayer: Player, firstArmies: Int,
        secondPlayer: Player, secondArmies: Int
    ) {
        assertPlayerDoesNotOccupy(country, firstPlayer)
        assertPlayerDoesNotOccupy(country, secondPlayer)
        occupations[country] =
            SharedOccupation(firstPlayer, firstArmies, secondPlayer, secondArmies)
    }
}

/**
 * Checks if the player occupies an Occupation.
 */
private class PlayerOccupationChecker(private val player: Player) : OccupationVisitor {
    private var playerOccupationFlag = false

    fun doesPlayerOccupy(occupation: Occupation): Boolean {
        occupation.accept(this)
        return playerOccupationFlag
    }

    override fun visit(occupation: NoOccupation) {
        playerOccupationFlag = false
    }

    override fun visit(occupation: SinglePlayerOccupation) {
        playerOccupationFlag = player == occupation.occupier
    }

    override fun visit(occupation: SharedOccupation) {
        playerOccupationFlag = player in occupation.occupiers
    }
}

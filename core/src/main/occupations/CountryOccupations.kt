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

    fun remove(country: Country, player: Player) {
        PlayerRemover(country, player).remove()
    }

    /**
     * Removes the player from a country if possible.
     */
    private inner class PlayerRemover(
        private val country: Country, private val player: Player) : OccupationVisitor {

        fun remove() {
            occupations.getValue(country).accept(this)
        }

        override fun visit(occupation: NoOccupation) {
            throw NotOccupyingPlayerException(player)
        }

        override fun visit(occupation: SinglePlayerOccupation) {
            throw CantRemoveOnlyOccupierException(country, player)
        }

        override fun visit(occupation: SharedOccupation) {
            occupations[country] = occupation.removePlayer(player)
        }
    }
}

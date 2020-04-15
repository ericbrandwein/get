package combat

import Country
import Player
import PositiveInt
import occupations.CountryOccupations

class AttackOccupations(
    private val countryOccupations: CountryOccupations,
    private val attackingCountry: Country,
    private val defendingCountry: Country
) {
    fun attackingPlayer() = countryOccupations.occupierOf(attackingCountry)
    fun armiesOfAttacker() = countryOccupations.armiesOf(attackingCountry)
    fun armiesOfDefender() = countryOccupations.armiesOf(defendingCountry)
    fun removeArmiesFromAttacker(armies: Int) = removeArmies(armies, attackingCountry)
    fun removeArmiesFromDefender(armies: Int) = removeArmies(armies, defendingCountry)

    private fun removeArmies(armiesLost: Int, country: Country) {
        if (armiesLost != 0) {
            countryOccupations.removeArmies(country, PositiveInt(armiesLost))
        }
    }

    fun occupyDefendingCountry(player: Player, armies: PositiveInt) =
        countryOccupations.occupy(defendingCountry, player, armies)
}

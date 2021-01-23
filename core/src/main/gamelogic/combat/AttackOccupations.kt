package gamelogic.combat

import Country
import PositiveInt
import gamelogic.occupations.CountryOccupations

class AttackOccupations(
    private val countryOccupations: CountryOccupations,
    private val attackingCountry: Country,
    private val defendingCountry: Country
) {
    private fun attackingPlayer() = countryOccupations.occupierOf(attackingCountry)
    fun armiesOfAttacker() = countryOccupations.armiesOf(attackingCountry)
    fun armiesOfDefender() = countryOccupations.armiesOf(defendingCountry)
    fun removeArmiesFromAttacker(armies: Int) = removeArmies(armies, attackingCountry)
    fun removeArmiesFromDefender(armies: Int) = removeArmies(armies, defendingCountry)

    private fun removeArmies(armiesLost: Int, country: Country) {
        if (armiesLost != 0) {
            countryOccupations.removeArmies(country, PositiveInt(armiesLost))
        }
    }

    fun occupyDefendingCountryWithAttackingPlayer(armies: PositiveInt) =
        countryOccupations.occupy(defendingCountry, attackingPlayer(), armies)
}

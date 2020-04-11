package combat

import Country
import PositiveInt
import occupations.CountryOccupations

/**
 * Applies the results of a combat to the combating countries occupations,
 * removing the corresponding armies and conquering the defending country if necessary.
 */
class CombatResultsApplier(private val countryOccupations: CountryOccupations) {
    fun apply(
        results: CombatResults, attackingCountry: Country, defendingCountry: Country
    ) {
        val armiesLostByDefender = results.armiesLostByDefender
        val armiesLostByAttacker = results.armiesLostByAttacker
        removeArmies(armiesLostByAttacker, attackingCountry)
        val defenderArmies = countryOccupations.armiesOf(defendingCountry).toInt()
        if (armiesLostByDefender == defenderArmies) {
            occupyDefendingCountry(attackingCountry, defendingCountry)
        } else {
            removeArmies(armiesLostByDefender, defendingCountry)
        }
    }

    private fun occupyDefendingCountry(
        attackingCountry: Country, defendingCountry: Country
    ) {
        removeArmies(OCCUPYING_ARMIES.toInt(), attackingCountry)
        val attackingPlayer = countryOccupations.occupierOf(attackingCountry)
        countryOccupations.occupy(defendingCountry, attackingPlayer, OCCUPYING_ARMIES)
    }

    private fun removeArmies(armiesLost: Int, country: Country) {
        if (armiesLost > 0) {
            countryOccupations.removeArmies(country, PositiveInt(armiesLost))
        }
    }

    companion object {
        private val OCCUPYING_ARMIES = PositiveInt(1)
    }
}

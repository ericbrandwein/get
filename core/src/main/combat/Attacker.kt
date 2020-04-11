package combat

import Country
import PositiveInt
import combat.resolver.CombatResolver
import occupations.CountryOccupations

class Attacker(
    private val countryOccupations: CountryOccupations,
    private val combatResolver: CombatResolver
) {
    fun attack(from: Country, to: Country) {
        val attackerArmies = countryOccupations.armiesOf(from)
        val defenderArmies = countryOccupations.armiesOf(to)
        val armiesToRemove = combatResolver.combat(attackerArmies, defenderArmies)
        val armiesLostByDefender = armiesToRemove.armiesLostByDefender
        val armiesLostByAttacker = armiesToRemove.armiesLostByAttacker
        removeArmies(armiesLostByAttacker, from)
        if (armiesLostByDefender == defenderArmies.toInt()) {
            occupyDefendingCountry(from, to)
        } else {
            removeArmies(armiesLostByDefender, to)
        }
    }

    private fun occupyDefendingCountry(from: Country, to: Country) {
        removeArmies(OCCUPYING_ARMIES.toInt(), from)
        val attackingPlayer = countryOccupations.occupierOf(from)
        countryOccupations.occupy(to, attackingPlayer, OCCUPYING_ARMIES)
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

package combat

import Country
import combat.resolver.CombatResolver
import occupations.CountryOccupations

class Attacker(
    private val countryOccupations: CountryOccupations,
    private val combatResolver: CombatResolver
) {
    private val resultsApplier = CombatResultsApplier(countryOccupations)

    fun attack(from: Country, to: Country) {
        val combatResults = resolveCombat(from, to)
        resultsApplier.apply(combatResults, from, to)
    }

    private fun resolveCombat(from: Country, to: Country): CombatResults {
        val attackerArmies = countryOccupations.armiesOf(from)
        val defenderArmies = countryOccupations.armiesOf(to)
        return combatResolver.combat(attackerArmies, defenderArmies)
    }
}

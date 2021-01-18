package combat

import Country
import combat.resolver.CombatResolver
import occupations.CountryOccupations

class Attacker(
    private val countryOccupations: CountryOccupations,
    private val combatResolver: CombatResolver
) {
    fun attack(from: Country, to: Country, conqueror: Conqueror): CombatResults {
        val attackingOccupations = AttackOccupations(countryOccupations, from, to)
        return Attack(attackingOccupations, combatResolver, conqueror).run()
    }
}

package gamelogic.combat

import Country
import gamelogic.combat.resolver.CombatResolver
import gamelogic.occupations.CountryOccupations

class Attacker(
    private val countryOccupations: CountryOccupations,
    private val combatResolver: CombatResolver
) {
    fun attack(from: Country, to: Country, conqueror: Conqueror): CombatResults {
        val attackingOccupations = AttackOccupations(countryOccupations, from, to)
        return Attack(attackingOccupations, combatResolver, conqueror).run()
    }
}

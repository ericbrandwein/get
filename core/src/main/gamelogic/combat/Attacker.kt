package gamelogic.combat

import Country
import gamelogic.combat.resolver.CombatResolver
import gamelogic.occupations.CountryOccupations

class Attacker(
    private val countryOccupations: CountryOccupations,
    private val combatResolver: CombatResolver
) {
    fun makeAttack(from: Country, to: Country): Attack {
        val attackingOccupations = AttackOccupations(countryOccupations, from, to)
        return Attack(attackingOccupations, combatResolver)
    }
}

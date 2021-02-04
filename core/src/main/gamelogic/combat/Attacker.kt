package gamelogic.combat

import Country
import gamelogic.combat.resolver.CombatResolver
import gamelogic.occupations.CountryOccupations

/**
 * Creates [Attack] objects that can be applied to some [countryOccupations].
 *
 * @param countryOccupations In which to apply the attacks.
 * @param combatResolver The resolver of the attacks.
 *
 * @see Attack
 */
class Attacker(
    private val countryOccupations: CountryOccupations,
    private val combatResolver: CombatResolver
) {
    fun makeAttack(from: Country, to: Country): Attack {
        val attackingOccupations = AttackOccupations(countryOccupations, from, to)
        return Attack(attackingOccupations, combatResolver)
    }
}

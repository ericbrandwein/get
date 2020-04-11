package combat.resolver

import PositiveInt
import combat.CombatResults

class FixedCombatResolver(private val combatResults: CombatResults) : CombatResolver {
    override fun combat(attackingArmies: PositiveInt, defendingArmies: PositiveInt) =
        combatResults
}

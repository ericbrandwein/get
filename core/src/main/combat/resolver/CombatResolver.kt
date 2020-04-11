package combat.resolver

import PositiveInt
import combat.CombatResults

interface CombatResolver {
    fun combat(
        attackingArmies: PositiveInt, defendingArmies: PositiveInt
    ): CombatResults
}

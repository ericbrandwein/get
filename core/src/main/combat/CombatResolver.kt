package combat

import PositiveInt

interface CombatResolver {
    fun combat(
        attackingArmies: PositiveInt, defendingArmies: PositiveInt
    ): CombatResults
}

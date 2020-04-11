package combat

import PositiveInt

interface CombatResolver {
    fun armiesLostForCombat(
        attackingArmies: PositiveInt, defendingArmies: PositiveInt
    ): Pair<Int, Int>
}

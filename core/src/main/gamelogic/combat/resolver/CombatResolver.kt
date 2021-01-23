package gamelogic.combat.resolver

import PositiveInt
import gamelogic.combat.CombatResults

interface CombatResolver {
    fun combat(
        attackingArmies: PositiveInt, defendingArmies: PositiveInt
    ): CombatResults
}

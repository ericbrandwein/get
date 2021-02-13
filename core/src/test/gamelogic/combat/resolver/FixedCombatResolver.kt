package gamelogic.combat.resolver

import PositiveInt
import gamelogic.combat.CombatResults

class FixedCombatResolver(
    private val expectedAttackingArmies: PositiveInt,
    private val expectedDefendingArmies: PositiveInt,
    private val combatResults: CombatResults
) : CombatResolver {
    private val defaultCombatResults =
        CombatResults(Pair(0, 0), Pair(listOf(0), listOf(0)))

    override fun combat(
        attackingArmies: PositiveInt, defendingArmies: PositiveInt
    ): CombatResults {
        return if (areExpectedArmies(attackingArmies, defendingArmies)) {
            combatResults
        } else {
            defaultCombatResults
        }
    }

    private fun areExpectedArmies(
        attackingArmies: PositiveInt, defendingArmies: PositiveInt
    ): Boolean {
        return attackingArmies == expectedAttackingArmies &&
            defendingArmies == expectedDefendingArmies
    }

    companion object {
        fun createEmpty(): FixedCombatResolver {
            val combatResults = CombatResults(Pair(1, 0), Pair(listOf(1), listOf(1)))
            return FixedCombatResolver(PositiveInt(1), PositiveInt(1), combatResults)
        }
    }
}

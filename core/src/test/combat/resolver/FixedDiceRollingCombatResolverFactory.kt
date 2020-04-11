package combat.resolver

import combat.CombatDiceRoller
import combat.diceCalculators.ClassicCombatDiceAmountCalculator
import dice.FixedDie

class FixedDiceRollingCombatResolverFactory {
    fun resolverWithDieRolls(
        attackerRolls: Collection<Int>, defenderRolls: Collection<Int>,
        vararg extraRolls: Int
    ): CombatResolver {
        val rolls = attackerRolls + defenderRolls + extraRolls.toTypedArray()
        return resolverWithDieRolls(*rolls.toIntArray())
    }

    fun resolverWithDieRolls(vararg rolls: Int): CombatResolver {
        val combatDiceRoller =
            CombatDiceRoller(ClassicCombatDiceAmountCalculator(), FixedDie(*rolls))
        return DiceRollingCombatResolver(combatDiceRoller)
    }
}

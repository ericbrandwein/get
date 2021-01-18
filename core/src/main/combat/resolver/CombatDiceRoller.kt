package combat.resolver

import PositiveInt
import combat.DiceAmountCalculator
import dice.Die

class CombatDiceRoller(
    private val diceAmountCalculator: DiceAmountCalculator, private val die: Die) {

    fun forCombat(
        attackingArmies: PositiveInt, defendingArmies: PositiveInt
    ): Pair<Collection<Int>, Collection<Int>> {
        val (attackerDice, defenderDice) =
            diceAmountCalculator.forAttack(attackingArmies, defendingArmies)
        val attackerRolls = roll(attackerDice)
        val defenderRolls = roll(defenderDice)
        return Pair(attackerRolls, defenderRolls)
    }

    private fun roll(amount: PositiveInt) = die.roll(amount.toInt())
}

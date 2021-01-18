package situations.classicCombat

import combat.DiceAmountCalculator
import PositiveInt as Pos

class ClassicCombatDiceAmountCalculator : DiceAmountCalculator() {

    override fun forAttackWithValidArmies(
        attackingArmies: Pos, defendingArmies: Pos
    ): Pair<Pos, Pos> {
        val attackingAmount = forAttacker(attackingArmies, defendingArmies)
        val defendingAmount = forDefender(defendingArmies)
        return Pair(attackingAmount, defendingAmount)
    }

    private fun forAttacker(attackingArmies: Pos, defendingArmies: Pos): Pos {
        var dice = minOf(MAXIMUM_DICE_AMOUNT, attackingArmies - Pos(1))
        if (shouldAddOneDieToAttacker(attackingArmies, defendingArmies)) {
            dice++
        }
        return dice
    }

    private fun shouldAddOneDieToAttacker(attackingArmies: Pos, defendingArmies: Pos) =
        attackingArmies >= Pos(2) * defendingArmies && defendingArmies >= Pos(3)

    private fun forDefender(defendingArmies: Pos): Pos =
        minOf(MAXIMUM_DICE_AMOUNT, defendingArmies)

    companion object {
        private val MAXIMUM_DICE_AMOUNT = Pos(3)
    }
}

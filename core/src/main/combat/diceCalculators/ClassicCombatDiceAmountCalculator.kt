package combat.diceCalculators

import PositiveInt as Pos

class ClassicCombatDiceAmountCalculator(
    private val attackingArmies: Pos, private val defendingArmies: Pos
) : DiceAmountCalculator {

    override fun forAttacker(): Pos {
        var dice = minOf(MAXIMUM_DICE_AMOUNT, attackingArmies - Pos(1))
        if (shouldAddOneDieToAttacker()) {
            dice++
        }
        return dice
    }

    private fun shouldAddOneDieToAttacker() =
        attackingArmies >= Pos(2) * defendingArmies && defendingArmies >= Pos(3)

    override fun forDefender() = minOf(MAXIMUM_DICE_AMOUNT, defendingArmies)

    companion object {
        private val MAXIMUM_DICE_AMOUNT = Pos(3)
    }
}

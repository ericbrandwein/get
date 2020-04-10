package situations

import PositiveInt

class ClassicCombatDiceAmountCalculator(
    private val attackingArmies: PositiveInt, private val defendingArmies: PositiveInt) {

    fun forAttacker(): PositiveInt {
        var dice = boundByMaximumDiceAmount(attackingArmies - PositiveInt(1))
        if (shouldAddOneDieToAttacker()) {
            dice++
        }
        return dice
    }

    private fun boundByMaximumDiceAmount(dice: PositiveInt) =
        minOf(MAXIMUM_DICE_AMOUNT, dice)

    private fun shouldAddOneDieToAttacker() =
        attackerHasAtLeastDoubleTheArmies() && defendingArmies >= PositiveInt(3)

    private fun attackerHasAtLeastDoubleTheArmies() =
        attackingArmies >= PositiveInt(2) * defendingArmies

    fun forDefender() = boundByMaximumDiceAmount(defendingArmies)

    companion object {
        private val MAXIMUM_DICE_AMOUNT = PositiveInt(3)
    }
}

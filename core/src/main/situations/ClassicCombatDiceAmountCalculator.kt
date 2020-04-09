package situations

import kotlin.math.min

class ClassicCombatDiceAmountCalculator(
    private val attackingArmies: Int, private val defendingArmies: Int) {

    fun forAttacker(): Int {
        var dice = min(MAXIMUM_DICE_AMOUNT, attackingArmies - 1)
        if (shouldAddOneDieToAttacker()) {
            dice++
        }
        return dice
    }

    private fun shouldAddOneDieToAttacker() =
        attackingArmies >= 2 * defendingArmies && defendingArmies >= 3

    companion object {
        private const val MAXIMUM_DICE_AMOUNT = 3
    }

}

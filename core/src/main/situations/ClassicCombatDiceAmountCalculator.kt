package situations

import PositiveArmiesAsserter
import kotlin.math.min

class ClassicCombatDiceAmountCalculator(
    private val attackingArmies: Int, private val defendingArmies: Int) {

    init {
        val asserter = PositiveArmiesAsserter()
        asserter.assertPositive(attackingArmies)
        asserter.assertPositive(defendingArmies)
    }

    fun forAttacker(): Int {
        var dice = boundByMaximumDiceAmount(attackingArmies - 1)
        if (shouldAddOneDieToAttacker()) {
            dice++
        }
        return dice
    }

    private fun boundByMaximumDiceAmount(dice: Int) = min(MAXIMUM_DICE_AMOUNT, dice)

    private fun shouldAddOneDieToAttacker() =
        attackingArmies >= 2 * defendingArmies && defendingArmies >= 3

    fun forDefender() = boundByMaximumDiceAmount(defendingArmies)

    companion object {
        private const val MAXIMUM_DICE_AMOUNT = 3
    }
}

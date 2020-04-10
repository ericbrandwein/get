package situations

import PositiveInt as Pos

class ClassicCombatDiceAmountCalculator(
    private val attackingArmies: Pos, private val defendingArmies: Pos) {

    fun forAttacker(): Pos {
        var dice = boundByMaximumDiceAmount(attackingArmies - Pos(1))
        if (shouldAddOneDieToAttacker()) {
            dice++
        }
        return dice
    }

    private fun boundByMaximumDiceAmount(dice: Pos) =
        minOf(MAXIMUM_DICE_AMOUNT, dice)

    private fun shouldAddOneDieToAttacker() =
        attackingArmies >= Pos(2) * defendingArmies && defendingArmies >= Pos(3)

    fun forDefender() = boundByMaximumDiceAmount(defendingArmies)

    companion object {
        private val MAXIMUM_DICE_AMOUNT = Pos(3)
    }
}

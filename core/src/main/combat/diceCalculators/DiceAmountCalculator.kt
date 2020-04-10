package combat.diceCalculators

import PositiveInt
import combat.NotEnoughArmiesForAttackException


abstract class DiceAmountCalculator(
    protected val attackingArmies: PositiveInt,
    protected val defendingArmies: PositiveInt
) {

    init {
        assertEnoughAttackerArmies()
    }

    private fun assertEnoughAttackerArmies() {
        if (attackingArmies < ATTACKER_MINIMUM_ARMIES) {
            throw NotEnoughArmiesForAttackException()
        }
    }

    abstract fun forAttacker(): PositiveInt
    abstract fun forDefender(): PositiveInt

    companion object {
        val ATTACKER_MINIMUM_ARMIES = PositiveInt(2)
    }
}

package gamelogic.combat

import PositiveInt


abstract class DiceAmountCalculator {

    fun forAttack(
        attackingArmies: PositiveInt, defendingArmies: PositiveInt
    ): Pair<PositiveInt, PositiveInt> {
        assertEnoughAttackingArmies(attackingArmies)
        return forAttackWithValidArmies(attackingArmies, defendingArmies)
    }

    private fun assertEnoughAttackingArmies(attackingArmies: PositiveInt) {
        if (attackingArmies < ATTACKER_MINIMUM_ARMIES) {
            throw NotEnoughArmiesForAttackException()
        }
    }

    protected abstract fun forAttackWithValidArmies(
        attackingArmies: PositiveInt, defendingArmies: PositiveInt
    ): Pair<PositiveInt, PositiveInt>

    companion object {
        val ATTACKER_MINIMUM_ARMIES = PositiveInt(2)
    }
}

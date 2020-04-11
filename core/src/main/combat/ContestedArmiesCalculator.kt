package combat

import PositiveInt

/**
 * Determines how many armies are going to be lost in total in a combat.
 */
class ContestedArmiesCalculator(
    private val attackingArmies: PositiveInt, private val defendingArmies: PositiveInt) {

    fun getArmies(): PositiveInt {
        val maxAttackerContestedArmies = attackingArmies - PositiveInt(1)
        return minOf(
            maxAttackerContestedArmies,
            defendingArmies,
            MAX_CONTESTED_ARMIES
        )
    }

    companion object {
        val MAX_CONTESTED_ARMIES = PositiveInt(3)
    }
}

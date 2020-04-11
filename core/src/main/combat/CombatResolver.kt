package combat

import PositiveInt
import combat.diceCalculators.DiceAmountCalculator
import combat.lostArmiesCalculator.LostArmiesCalculator
import dice.Die

/**
 * Rolls dice and determines the attacker's and defender's lost armies
 * based on the results.
 */
class CombatResolver(
    private val diceAmountCalculator: DiceAmountCalculator, private val die: Die
) {

    private val lostArmiesCalculator = LostArmiesCalculator()

    fun combat(
        attackingArmies: PositiveInt, defendingArmies: PositiveInt
    ): Pair<Int, Int> {
        val (attackerRolls, defenderRolls) =
            rollDiceForCombat(attackingArmies, defendingArmies)
        val contestedArmies = calculateContestedArmies(attackingArmies, defendingArmies)
        return lostArmiesCalculator.armiesLostForRolls(
            attackerRolls, defenderRolls, contestedArmies)
    }

    private fun calculateContestedArmies(
        attackingArmies: PositiveInt, defendingArmies: PositiveInt) =
        ContestedArmiesCalculator(attackingArmies, defendingArmies).getArmies()

    private fun rollDiceForCombat(
        attackingArmies: PositiveInt, defendingArmies: PositiveInt) =
        CombatDiceRoller(diceAmountCalculator, die).forCombat(attackingArmies, defendingArmies)

}

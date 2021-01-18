package combat.lostArmiesCalculator

import PositiveInt
import kotlin.math.min

/**
 * Calculates the armies lost by the attacker and the defender,
 * given the rolls passed as a parameter.
 *
 * The lost armies total is equal to the amount contested.
 */
fun calculateArmiesLostForRolls(
    attackerRolls: Collection<Int>, defenderRolls: Collection<Int>,
    contestedArmies: PositiveInt
): Pair<Int, Int> {

    assertEnoughDiceForContestedArmies(
        attackerRolls.size, defenderRolls.size, contestedArmies)

    val armiesLostByDefender = min(
        rollsLostByDefender(attackerRolls, defenderRolls),
        contestedArmies.toInt()
    )
    val armiesLostByAttacker = contestedArmies.toInt() - armiesLostByDefender
    return Pair(armiesLostByAttacker, armiesLostByDefender)
}

private fun assertEnoughDiceForContestedArmies(
    attackerDice: Int, defenderDice: Int, contestedArmies: PositiveInt
) {
    if (min(attackerDice, defenderDice) < contestedArmies.toInt()) {
        throw TooManyArmiesContestedException(contestedArmies)
    }
}

private fun rollsLostByDefender(
    attackerRolls: Collection<Int>, defenderRolls: Collection<Int>
): Int {
    val sortedAttackerRolls = attackerRolls.sortedDescending()
    val sortedDefenderRolls = defenderRolls.sortedDescending()
    val pairedRolls = sortedAttackerRolls.zip(sortedDefenderRolls)
    return pairedRolls.count { (attackerResult, defenderResult) ->
        attackerResult > defenderResult
    }
}


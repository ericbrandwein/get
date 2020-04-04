package combat

import kotlin.math.min

/**
 * Resolves combats between an attacker and a defender,
 * using the rolls passed as a parameter to determine how many armies each one lost.
 *
 * The lost armies total is equal to the amount contested.
 */
class CombatResolver {
    fun combat(
        attackerRolls: Collection<Int>, defenderRolls: Collection<Int>,
        contestedArmies: Int
    ): Pair<Int, Int> {

        assertPositiveAmountOfArmiesContested(contestedArmies)
        assertEnoughDiceForContestedArmies(
            attackerRolls.size, defenderRolls.size, contestedArmies)

        val armiesLostByDefender = min(
            rollsLostByDefender(attackerRolls, defenderRolls),
            contestedArmies
        )
        val armiesLostByAttacker = contestedArmies - armiesLostByDefender
        return Pair(armiesLostByAttacker, armiesLostByDefender)
    }

    private fun assertPositiveAmountOfArmiesContested(contestedArmies: Int) {
        if (contestedArmies <= 0) {
            throw NonPositiveArmiesContestedException(contestedArmies)
        }
    }

    private fun assertEnoughDiceForContestedArmies(
        attackerDice: Int, defenderDice: Int, contestedArmies: Int) {
        if (min(attackerDice, defenderDice) < contestedArmies) {
            throw TooManyArmiesContestedException(contestedArmies)
        }
    }

    private fun rollsLostByDefender(
        attackerRolls: Collection<Int>, defenderRolls: Collection<Int>): Int {
        val sortedAttackerRolls = attackerRolls.sortedDescending()
        val sortedDefenderRolls = defenderRolls.sortedDescending()
        val pairedRolls = sortedAttackerRolls.zip(sortedDefenderRolls)
        return pairedRolls.count { (attackerResult, defenderResult) ->
            attackerResult > defenderResult
        }
    }
}

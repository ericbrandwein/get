package combat

import PositiveInt
import combat.lostArmiesCalculator.LostArmiesCalculator

class DiceRollingCombatResolver(
    private val diceRoller: CombatDiceRoller
) : CombatResolver {

    private val lostArmiesCalculator = LostArmiesCalculator()

    override fun combat(
        attackingArmies: PositiveInt, defendingArmies: PositiveInt
    ): CombatResults {
        val rolls = diceRoller.forCombat(attackingArmies, defendingArmies)
        val attackerRolls = rolls.first
        val defenderRolls = rolls.second
        val contestedArmies =
            ContestedArmiesCalculator(attackingArmies, defendingArmies).getArmies()
        val lostArmies = lostArmiesCalculator.armiesLostForRolls(
            attackerRolls, defenderRolls, contestedArmies)
        return CombatResults(lostArmies, rolls)
    }
}

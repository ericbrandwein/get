package combat

import PositiveInt
import combat.lostArmiesCalculator.calculateArmiesLostForRolls

class DiceRollingCombatResolver(
    private val diceRoller: CombatDiceRoller
) : CombatResolver {

    override fun combat(
        attackingArmies: PositiveInt, defendingArmies: PositiveInt
    ): CombatResults {
        val rolls = diceRoller.forCombat(attackingArmies, defendingArmies)
        val attackerRolls = rolls.first
        val defenderRolls = rolls.second
        val contestedArmies = calculateContestedArmies(attackingArmies, defendingArmies)
        val lostArmies =
            calculateArmiesLostForRolls(attackerRolls, defenderRolls, contestedArmies)
        return CombatResults(lostArmies, rolls)
    }
}

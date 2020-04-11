package combat

import PositiveInt
import combat.lostArmiesCalculator.LostArmiesCalculator

class DiceRollingCombatResolver(
    private val diceRoller: CombatDiceRoller
) : CombatResolver {

    private val lostArmiesCalculator = LostArmiesCalculator()

    override fun armiesLostForCombat(
        attackingArmies: PositiveInt, defendingArmies: PositiveInt
    ): Pair<Int, Int> {
        val (attackerRolls, defenderRolls) =
            diceRoller.forCombat(attackingArmies, defendingArmies)
        val contestedArmies =
            ContestedArmiesCalculator(attackingArmies, defendingArmies).getArmies()
        return lostArmiesCalculator.armiesLostForRolls(
            attackerRolls, defenderRolls, contestedArmies)
    }
}

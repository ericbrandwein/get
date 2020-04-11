package combat

import PositiveInt
import combat.lostArmiesCalculator.LostArmiesCalculator

class CombatResolver(private val diceRoller: CombatDiceRoller) {

    private val lostArmiesCalculator = LostArmiesCalculator()

    fun armiesLostForCombat(
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

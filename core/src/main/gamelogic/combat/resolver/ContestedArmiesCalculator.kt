package gamelogic.combat.resolver

import PositiveInt

/**
 * Determines how many armies are going to be lost in total in a gamelogic.combat.
 */
fun calculateContestedArmies(
    attackingArmies: PositiveInt, defendingArmies: PositiveInt
): PositiveInt {
    val maxAttackerContestedArmies = attackingArmies - PositiveInt(1)
    return minOf(
        maxAttackerContestedArmies,
        defendingArmies,
        MAX_CONTESTED_ARMIES
    )
}

val MAX_CONTESTED_ARMIES = PositiveInt(3)


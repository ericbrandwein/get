package gamelogic.combat

import gamelogic.combat.resolver.CombatDiceRoller
import gamelogic.combat.resolver.DiceRollingCombatResolver
import gamelogic.dice.RandomDie
import gamelogic.occupations.CountryOccupations

interface AttackerFactory {
    fun create(
        occupations: CountryOccupations, diceAmountCalculator: DiceAmountCalculator
    ): Attacker
}

class DiceRollingAttackerFactory : AttackerFactory {
    override fun create(
        occupations: CountryOccupations, diceAmountCalculator: DiceAmountCalculator
    ): Attacker {
        val diceRoller = CombatDiceRoller(diceAmountCalculator, RandomDie())
        val combatResolver = DiceRollingCombatResolver(diceRoller)
        return Attacker(occupations, combatResolver)
    }
}

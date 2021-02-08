package gamelogic.combat

import PositiveInt
import gamelogic.combat.resolver.FixedCombatResolver
import gamelogic.occupations.CountryOccupations

class AttackingCountryWinsAttackerFactory(
    private val expectedAttackingArmies: PositiveInt,
    private val expectedDefendingArmies: PositiveInt
) : AttackerFactory {
    override fun create(
        occupations: CountryOccupations, diceAmountCalculator: DiceAmountCalculator
    ): Attacker {
        val combatResults = CombatResults(Pair(0, 1), Pair(listOf(6), listOf(5)))
        val combatResolver = FixedCombatResolver(
            expectedAttackingArmies, expectedDefendingArmies, combatResults)
        return Attacker(occupations, combatResolver)
    }
}

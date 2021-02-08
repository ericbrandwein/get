package gamelogic.combat

import Country
import PositiveInt
import gamelogic.combat.resolver.CombatDiceRoller
import gamelogic.combat.resolver.CombatResolver
import gamelogic.combat.resolver.DiceRollingCombatResolver
import gamelogic.dice.RandomDie
import gamelogic.occupations.CountryOccupations
import gamelogic.occupations.EmptyCountryException

/**
 * Creates [Attack] objects that can be applied to some [occupations].
 *
 * @param occupations In which to apply the attacks.
 * @param combatResolver The resolver of the attacks.
 *
 * @see Attack
 */
class Attacker(
    private val occupations: CountryOccupations,
    private val combatResolver: CombatResolver
) {
    fun attack(from: Country, to: Country) {
        assertCountryIsOccupied(from)
        assertCountryIsOccupied(to)
        val armiesOfAttacker = occupations.armiesOf(from)
        val armiesOfDefender = occupations.armiesOf(to)
        val combatResults = combatResolver.combat(
            PositiveInt(armiesOfAttacker), PositiveInt(armiesOfDefender)
        )
        removeArmies(from, combatResults.armiesLostByAttacker)
        removeArmies(to, combatResults.armiesLostByDefender)
    }

    private fun removeArmies(country: Country, armies: Int) {
        if (armies > 0) {
            occupations.removeArmies(country, PositiveInt(armies))
        }
    }

    private fun assertCountryIsOccupied(country: Country) {
        if (occupations.isEmpty(country)) {
            throw EmptyCountryException(country)
        }
    }

    companion object {
        fun withDiceAmountCalculator(
            countryOccupations: CountryOccupations,
            diceAmountCalculator: DiceAmountCalculator
        ): Attacker {
            val diceRoller = CombatDiceRoller(diceAmountCalculator, RandomDie())
            val combatResolver = DiceRollingCombatResolver(diceRoller)
            return Attacker(countryOccupations, combatResolver)
        }
    }
}

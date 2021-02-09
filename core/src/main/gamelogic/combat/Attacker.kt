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
 * Preforms attacks between two countries.
 *
 * @param occupations In which to apply the attacks.
 * @param combatResolver The resolver of the attacks.
 */
class Attacker(
    private val occupations: CountryOccupations,
    private val combatResolver: CombatResolver
) {
    fun attack(from: Country, to: Country): CombatResults {
        assertCountryIsOccupied(from)
        assertCountryIsOccupied(to)
        assertDifferentOccupiers(from, to)
        val armiesOfAttacker = occupations.armiesOf(from)
        val armiesOfDefender = occupations.armiesOf(to)
        val combatResults = combatResolver.combat(
            PositiveInt(armiesOfAttacker), PositiveInt(armiesOfDefender)
        )
        removeArmies(from, combatResults.armiesLostByAttacker)
        removeArmies(to, combatResults.armiesLostByDefender)
        return combatResults
    }

    private fun assertCountryIsOccupied(country: Country) {
        if (occupations.isEmpty(country)) {
            throw EmptyCountryException(country)
        }
    }

    private fun assertDifferentOccupiers(from: Country, to: Country) {
        if (occupations.occupierOf(from) == occupations.occupierOf(to)) {
            throw CannotAttackOwnCountryException(from, to)
        }
    }

    private fun removeArmies(country: Country, armies: Int) {
        if (armies > 0) {
            occupations.removeArmies(country, PositiveInt(armies))
        }
    }
}

class CannotAttackOwnCountryException(val from: Country, val to: Country) :
    Exception("Cannot attack country $to from $from; " +
        "the occupier of both countries is the same.")

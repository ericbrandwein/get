package combat

import combat.diceCalculators.ClassicCombatDiceAmountCalculator
import dice.FixedDie
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import PositiveInt as Pos

class DiceRollingCombatResolverTest {
    private fun resolverWithDieRolls(vararg rolls: Int): CombatResolver {
        val combatDiceRoller =
            CombatDiceRoller(ClassicCombatDiceAmountCalculator(), FixedDie(*rolls))
        return DiceRollingCombatResolver(combatDiceRoller)
    }

    @Test
    fun `Only attacker loses one army when losing one roll`() {
        val resolver = resolverWithDieRolls(1, 4)

        val (armiesLostByAttacker, armiesLostByDefender) = resolver.armiesLostForCombat(
            attackingArmies = Pos(2), defendingArmies = Pos(1))

        assertEquals(1, armiesLostByAttacker)
        assertEquals(0, armiesLostByDefender)
    }

    @Test
    fun `Only defender loses one army when losing one roll`() {
        val resolver = resolverWithDieRolls(4, 1)

        val (armiesLostByAttacker, armiesLostByDefender) = resolver.armiesLostForCombat(
            attackingArmies = Pos(2), defendingArmies = Pos(1))

        assertEquals(0, armiesLostByAttacker)
        assertEquals(1, armiesLostByDefender)
    }

    @Test
    fun `Attacker loses the amount of rolls lost when only he loses rolls`() {
        val resolver = resolverWithDieRolls(1, 1, 3, 4)

        val (armiesLostByAttacker, armiesLostByDefender) = resolver.armiesLostForCombat(
            attackingArmies = Pos(3), defendingArmies = Pos(2))

        assertEquals(2, armiesLostByAttacker)
        assertEquals(0, armiesLostByDefender)
    }

    @Test
    fun `Attacker and defender both lose armies when both lose rolls`() {
        val resolver = resolverWithDieRolls(2, 6, 6, 1)

        val (armiesLostByAttacker, armiesLostByDefender) = resolver.armiesLostForCombat(
            attackingArmies = Pos(3), defendingArmies = Pos(2))

        assertEquals(1, armiesLostByAttacker)
        assertEquals(1, armiesLostByDefender)
    }

    @Test
    fun `Contested armies are not more than the amount of attacking armies`() {
        val resolver = resolverWithDieRolls(6, 1, 1, 1)

        val (armiesLostByAttacker, armiesLostByDefender) = resolver.armiesLostForCombat(
            attackingArmies = Pos(2), defendingArmies = Pos(3))

        assertEquals(0, armiesLostByAttacker)
        assertEquals(1, armiesLostByDefender)
    }

    @Test
    fun `Doesn't roll more for the attacker than what the diceAmountCalculatorFactory tells it to`() {
        val resolver = resolverWithDieRolls(6, 5, 4, 1, 2, 6)

        val (armiesLostByAttacker, armiesLostByDefender) = resolver.armiesLostForCombat(
            attackingArmies = Pos(5), defendingArmies = Pos(2))

        assertEquals(0, armiesLostByAttacker)
        assertEquals(2, armiesLostByDefender)
    }

    @Test
    fun `Doesn't roll more for the defender than what the diceAmountCalculatorFactory tells it to`() {
        val resolver = resolverWithDieRolls(6, 1, 1, 1, 6)

        val (armiesLostByAttacker, armiesLostByDefender) = resolver.armiesLostForCombat(
            attackingArmies = Pos(2), defendingArmies = Pos(4))

        assertEquals(0, armiesLostByAttacker)
        assertEquals(1, armiesLostByDefender)
    }

    @Test
    fun `Can't contest more than 3 armies`() {
        val resolver = resolverWithDieRolls(1, 3, 5, 1, 2, 6)

        val (armiesLostByAttacker, armiesLostByDefender) = resolver.armiesLostForCombat(
            attackingArmies = Pos(5), defendingArmies = Pos(5))

        assertEquals(2, armiesLostByAttacker)
        assertEquals(1, armiesLostByDefender)
    }

    @Test
    fun `Can't attack with less than 2 armies`() {
        val resolver = resolverWithDieRolls(1)

        assertFailsWith<NotEnoughArmiesForAttackException> {
            resolver.armiesLostForCombat(
                attackingArmies = Pos(1), defendingArmies = Pos(2))
        }
    }
}

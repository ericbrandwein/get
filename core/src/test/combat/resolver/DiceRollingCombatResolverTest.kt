package combat.resolver

import combat.CombatDiceRoller
import combat.CombatResults
import combat.NotEnoughArmiesForAttackException
import combat.diceCalculators.ClassicCombatDiceAmountCalculator
import dice.FixedDie
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import PositiveInt as Pos

class DiceRollingCombatResolverTest {
    private fun resolverWithDieRolls(
        attackerRolls: Collection<Int>, defenderRolls: Collection<Int>,
        vararg extraRolls: Int
    ): CombatResolver {
        val rolls = attackerRolls + defenderRolls + extraRolls.toTypedArray()
        return resolverWithDieRolls(*rolls.toIntArray())
    }


    private fun resolverWithDieRolls(vararg rolls: Int): CombatResolver {
        val combatDiceRoller =
            CombatDiceRoller(
                ClassicCombatDiceAmountCalculator(), FixedDie(*rolls))
        return DiceRollingCombatResolver(
            combatDiceRoller)
    }

    private fun assertArmiesLostAre(
        lostByAttacker: Int, lostByDefender: Int, combatResults: CombatResults
    ) {
        assertEquals(lostByAttacker, combatResults.armiesLostByAttacker)
        assertEquals(lostByDefender, combatResults.armiesLostByDefender)
    }

    private fun assertRollsAre(
        attackerRolls: List<Int>,
        defenderRolls: List<Int>,
        combatResults: CombatResults
    ) {
        assertEquals(attackerRolls, combatResults.attackerRolls)
        assertEquals(defenderRolls, combatResults.defenderRolls)
    }

    @Test
    fun `Only attacker loses one army when losing one roll`() {
        val attackerRolls = listOf(1)
        val defenderRolls = listOf(4)
        val resolver = resolverWithDieRolls(attackerRolls, defenderRolls)

        val combatResults = resolver.combat(
            attackingArmies = Pos(2), defendingArmies = Pos(1))

        assertArmiesLostAre(1, 0, combatResults)
        assertRollsAre(attackerRolls, defenderRolls, combatResults)
    }

    @Test
    fun `Only defender loses one army when losing one roll`() {
        val attackerRolls = listOf(4)
        val defenderRolls = listOf(1)
        val resolver = resolverWithDieRolls(attackerRolls, defenderRolls)

        val combatResults = resolver.combat(
            attackingArmies = Pos(2), defendingArmies = Pos(1))

        assertArmiesLostAre(0, 1, combatResults)
        assertRollsAre(attackerRolls, defenderRolls, combatResults)
    }

    @Test
    fun `Attacker loses the amount of rolls lost when only he loses rolls`() {
        val attackerRolls = listOf(1, 1)
        val defenderRolls = listOf(3, 4)
        val resolver = resolverWithDieRolls(attackerRolls, defenderRolls)

        val combatResults = resolver.combat(
            attackingArmies = Pos(3), defendingArmies = Pos(2))

        assertArmiesLostAre(2, 0, combatResults)
        assertRollsAre(attackerRolls, defenderRolls, combatResults)
    }

    @Test
    fun `Attacker and defender both lose armies when both lose rolls`() {
        val attackerRolls = listOf(2, 6)
        val defenderRolls = listOf(6, 1)
        val resolver = resolverWithDieRolls(attackerRolls, defenderRolls)

        val combatResults = resolver.combat(
            attackingArmies = Pos(3), defendingArmies = Pos(2))

        assertArmiesLostAre(1, 1, combatResults)
        assertRollsAre(attackerRolls, defenderRolls, combatResults)
    }

    @Test
    fun `Contested armies are not more than the amount of attacking armies`() {
        val attackerRolls = listOf(6)
        val defenderRolls = listOf(1, 1, 1)
        val resolver = resolverWithDieRolls(attackerRolls, defenderRolls)

        val combatResults = resolver.combat(
            attackingArmies = Pos(2), defendingArmies = Pos(3))

        assertArmiesLostAre(0, 1, combatResults)
        assertRollsAre(attackerRolls, defenderRolls, combatResults)
    }

    @Test
    fun `Doesn't roll more for the attacker than what the diceAmountCalculatorFactory tells it to`() {
        val attackerRolls = listOf(6, 5, 4)
        val defenderRolls = listOf(1, 2)
        val resolver = resolverWithDieRolls(attackerRolls, defenderRolls, 6)

        val combatResults = resolver.combat(
            attackingArmies = Pos(5), defendingArmies = Pos(2))

        assertArmiesLostAre(0, 2, combatResults)
        assertRollsAre(attackerRolls, defenderRolls, combatResults)
    }

    @Test
    fun `Doesn't roll more for the defender than what the diceAmountCalculatorFactory tells it to`() {
        val attackerRolls = listOf(6)
        val defenderRolls = listOf(1, 1, 1)
        val resolver = resolverWithDieRolls(attackerRolls, defenderRolls, 6)

        val combatResults = resolver.combat(
            attackingArmies = Pos(2), defendingArmies = Pos(4))


        assertArmiesLostAre(0, 1, combatResults)
        assertRollsAre(attackerRolls, defenderRolls, combatResults)
    }

    @Test
    fun `Can't contest more than 3 armies`() {
        val attackerRolls = listOf(1, 3, 5)
        val defenderRolls = listOf(1, 2, 6)
        val resolver = resolverWithDieRolls(attackerRolls, defenderRolls)

        val combatResults = resolver.combat(
            attackingArmies = Pos(5), defendingArmies = Pos(5))

        assertArmiesLostAre(2, 1, combatResults)
        assertRollsAre(attackerRolls, defenderRolls, combatResults)
    }

    @Test
    fun `Can't attack with less than 2 armies`() {
        val resolver = resolverWithDieRolls(1)

        assertFailsWith<NotEnoughArmiesForAttackException> {
            resolver.combat(attackingArmies = Pos(1), defendingArmies = Pos(2))
        }
    }
}

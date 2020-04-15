package combat.resolver

import situations.classicCombat.ClassicCombatDiceAmountCalculator
import dice.FixedDie
import kotlin.test.Test
import kotlin.test.assertEquals
import PositiveInt as Pos

class CombatDiceRollerTest {
    @Test
    fun `Rolls one die for each if the calculator says so`() {
        val diceAmountCalculator = ClassicCombatDiceAmountCalculator()
        val die = FixedDie(1, 2)
        val roller = CombatDiceRoller(diceAmountCalculator, die)

        val (attackerRolls, defenderRolls) = roller.forCombat(Pos(2), Pos(1))

        assertEquals(1, attackerRolls.single())
        assertEquals(2, defenderRolls.single())
    }

    @Test
    fun `Rolls more than one die for attacker if the calculator says so`() {
        val diceAmountCalculator = ClassicCombatDiceAmountCalculator()
        val die = FixedDie(1, 2, 3)
        val roller = CombatDiceRoller(diceAmountCalculator, die)

        val (attackerRolls, defenderRolls) = roller.forCombat(Pos(3), Pos(1))

        assertEquals(listOf(1, 2), attackerRolls)
        assertEquals(3, defenderRolls.single())
    }

    @Test
    fun `Rolls more than one die for defender if the calculator says so`() {
        val die = FixedDie(1, 2, 3)
        val diceAmountCalculator = ClassicCombatDiceAmountCalculator()
        val roller = CombatDiceRoller(diceAmountCalculator, die)

        val (attackerRolls, defenderRolls) = roller.forCombat(Pos(2), Pos(2))

        assertEquals(1, attackerRolls.single())
        assertEquals(listOf(2, 3), defenderRolls)
    }
}

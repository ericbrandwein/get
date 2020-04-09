package situations

import kotlin.test.Test
import kotlin.test.assertEquals

class ClassicCombatDiceAmountCalculatorTest {
    @Test
    fun `Attacker dice amount for an attacker with two armies should be one`() {
        val calculator = ClassicCombatDiceAmountCalculator(2, 3)

        assertEquals(1, calculator.forAttacker())
    }

    @Test
    fun `Attacker dice amount for an attacker with 4 armies or less should be one less than the amount of armies`() {
        val calculator = ClassicCombatDiceAmountCalculator(3, 1)

        assertEquals(2, calculator.forAttacker())
    }

    @Test
    fun `Attacker dice amount for an attacker with five or more armies should be three`() {
        val calculator = ClassicCombatDiceAmountCalculator(5, 1)

        assertEquals(3, calculator.forAttacker())
    }

    @Test
    fun `Attacker dice amount when attacker has double the armies of the defender and defender has at least 3 armies should be 4`() {
        val calculator = ClassicCombatDiceAmountCalculator(6, 3)

        assertEquals(4, calculator.forAttacker())
    }

    @Test
    fun `Attacker dice amount when attacker has more than double the armies of the defender and defender has at least 3 armies should be 4`() {
        val calculator = ClassicCombatDiceAmountCalculator(9, 4)

        assertEquals(4, calculator.forAttacker())
    }
}

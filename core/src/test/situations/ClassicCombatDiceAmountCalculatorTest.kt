package situations

import NonPositiveArmiesException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ClassicCombatDiceAmountCalculatorTest {

    @Test
    fun `Can't create with a non-positive amount of attacking armies`() {
        val armies = -5

        val exception = assertFailsWith<NonPositiveArmiesException> {
            ClassicCombatDiceAmountCalculator(armies, 1)
        }

        assertEquals(armies, exception.armies)
    }

    @Test
    fun `Can't create with a non-positive amount of defending armies`() {
        val armies = -5

        val exception = assertFailsWith<NonPositiveArmiesException> {
            ClassicCombatDiceAmountCalculator(1, armies)
        }

        assertEquals(armies, exception.armies)
    }

    class ForAttacker {
        @Test
        fun `Dice amount for an attacker with two armies should be one`() {
            val calculator = ClassicCombatDiceAmountCalculator(2, 3)

            assertEquals(1, calculator.forAttacker())
        }

        @Test
        fun `Dice amount for an attacker with 4 armies or less should be one less than the amount of armies`() {
            val calculator = ClassicCombatDiceAmountCalculator(3, 1)

            assertEquals(2, calculator.forAttacker())
        }

        @Test
        fun `Dice amount for an attacker with five or more armies should be three`() {
            val calculator = ClassicCombatDiceAmountCalculator(5, 1)

            assertEquals(3, calculator.forAttacker())
        }

        @Test
        fun `Dice amount when attacker has double the armies of the defender and defender has at least 3 armies should be 4`() {
            val calculator = ClassicCombatDiceAmountCalculator(6, 3)

            assertEquals(4, calculator.forAttacker())
        }

        @Test
        fun `Dice amount when attacker has more than double the armies of the defender and defender has at least 3 armies should be 4`() {
            val calculator = ClassicCombatDiceAmountCalculator(9, 4)

            assertEquals(4, calculator.forAttacker())
        }
    }


    class ForDefender {
        @Test
        fun `Dice amount for a defender with one army should be one`() {
            val calculator = ClassicCombatDiceAmountCalculator(3, 1)

            assertEquals(1, calculator.forDefender())
        }

        @Test
        fun `Dice amount for a defender with 3 armies or less should be the amount of armies`() {
            val calculator = ClassicCombatDiceAmountCalculator(2, 3)

            assertEquals(3, calculator.forDefender())
        }

        @Test
        fun `Dice amount for a defender with 4 armies or more should be 3`() {
            val calculator = ClassicCombatDiceAmountCalculator(10, 4)

            assertEquals(3, calculator.forDefender())
        }
    }
}

package situations.classicCombat

import PositiveInt
import combat.NotEnoughArmiesForAttackException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ClassicCombatDiceAmountCalculatorTest {

    @Test
    fun `Can't calculate for an amount of attacking armies less than 2`() {
        assertFailsWith<NotEnoughArmiesForAttackException> {
            amountForAttack(1, 2)
        }
    }

    class ForAttacker {

        @Test
        fun `Dice amount for an attacker with two armies should be one`() {
            val (attackerAmount, _) = amountForAttack(2, 3)

            assertEquals(PositiveInt(1), attackerAmount)
        }

        @Test
        fun `Dice amount for an attacker with 4 armies or less should be one less than the amount of armies`() {
            val (attackerAmount, _) = amountForAttack(3, 1)

            assertEquals(PositiveInt(2), attackerAmount)
        }

        @Test
        fun `Dice amount for an attacker with five or more armies should be three`() {
            val (attackerAmount, _) = amountForAttack(5, 1)

            assertEquals(PositiveInt(3), attackerAmount)
        }

        @Test
        fun `Dice amount when attacker has double the armies of the defender and defender has at least 3 armies should be 4`() {
            val (attackerAmount, _) = amountForAttack(6, 3)

            assertEquals(PositiveInt(4), attackerAmount)
        }

        @Test
        fun `Dice amount when attacker has more than double the armies of the defender and defender has at least 3 armies should be 4`() {
            val (attackerAmount, _) = amountForAttack(9, 4)

            assertEquals(PositiveInt(4), attackerAmount)
        }

    }

    class ForDefender {

        @Test
        fun `Dice amount for a defender with one army should be one`() {
            val (_, defenderAmount) = amountForAttack(3, 1)

            assertEquals(PositiveInt(1), defenderAmount)
        }

        @Test
        fun `Dice amount for a defender with 3 armies or less should be the amount of armies`() {
            val (_, defenderAmount) = amountForAttack(2, 3)

            assertEquals(PositiveInt(3), defenderAmount)
        }

        @Test
        fun `Dice amount for a defender with 4 armies or more should be 3`() {
            val (_, defenderAmount) = amountForAttack(10, 4)

            assertEquals(PositiveInt(3), defenderAmount)
        }
    }

    companion object {
        private fun amountForAttack(attackingArmies: Int, defendingArmies: Int) =
            ClassicCombatDiceAmountCalculator().forAttack(
                PositiveInt(attackingArmies), PositiveInt(defendingArmies))
    }
}

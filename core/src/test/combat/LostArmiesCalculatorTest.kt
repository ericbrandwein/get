package combat

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import PositiveInt as Pos

class LostArmiesCalculatorTest {

    private lateinit var resolver: LostArmiesCalculator

    @BeforeTest
    fun setup() {
        resolver = LostArmiesCalculator()
    }

    @Test
    fun `Attacker wins when he rolls higher`() {
        val attackerRolls = listOf(6)
        val defenderRolls = listOf(1)
        val contestedArmies = Pos(1)

        val (lostAttackerArmies, lostDefenderArmies) =
            resolver.armiesLostForRolls(attackerRolls, defenderRolls, contestedArmies)

        assertEquals(0, lostAttackerArmies)
        assertEquals(1, lostDefenderArmies)
    }

    @Test
    fun `Defender wins when he rolls higher`() {
        val attackerRolls = listOf(1)
        val defenderRolls = listOf(6)
        val contestedArmies = Pos(1)

        val (lostAttackerArmies, lostDefenderArmies) =
            resolver.armiesLostForRolls(attackerRolls, defenderRolls, contestedArmies)

        assertEquals(1, lostAttackerArmies)
        assertEquals(0, lostDefenderArmies)
    }

    @Test
    fun `Defender wins when there's a tie`() {
        val attackerRolls = listOf(3)
        val defenderRolls = listOf(3)
        val contestedArmies = Pos(1)

        val (lostAttackerArmies, lostDefenderArmies) =
            resolver.armiesLostForRolls(attackerRolls, defenderRolls, contestedArmies)

        assertEquals(1, lostAttackerArmies)
        assertEquals(0, lostDefenderArmies)
    }

    @Test
    fun `Attacker can win more than one roll`() {
        val attackerRolls = listOf(6, 5)
        val defenderRolls = listOf(2, 1)
        val contestedArmies = Pos(2)

        val (lostAttackerArmies, lostDefenderArmies) =
            resolver.armiesLostForRolls(attackerRolls, defenderRolls, contestedArmies)

        assertEquals(0, lostAttackerArmies)
        assertEquals(2, lostDefenderArmies)
    }

    @Test
    fun `Attacker rolls are sorted before evaluating the results`() {
        val attackerRolls = listOf(1, 6)
        val defenderRolls = listOf(2)
        val contestedArmies = Pos(1)

        val (lostAttackerArmies, lostDefenderArmies) =
            resolver.armiesLostForRolls(attackerRolls, defenderRolls, contestedArmies)

        assertEquals(0, lostAttackerArmies)
        assertEquals(1, lostDefenderArmies)
    }

    @Test
    fun `Defender rolls are sorted before evaluating the results`() {
        val attackerRolls = listOf(6)
        val defenderRolls = listOf(2, 6)
        val contestedArmies = Pos(1)

        val (lostAttackerArmies, lostDefenderArmies) =
            resolver.armiesLostForRolls(attackerRolls, defenderRolls, contestedArmies)

        assertEquals(1, lostAttackerArmies)
        assertEquals(0, lostDefenderArmies)
    }

    @Test
    fun `Attacker can't win more than the contested armies`() {
        val attackerRolls = listOf(6, 3)
        val defenderRolls = listOf(1, 5)
        val contestedArmies = Pos(1)

        val (lostAttackerArmies, lostDefenderArmies) =
            resolver.armiesLostForRolls(attackerRolls, defenderRolls, contestedArmies)

        assertEquals(0, lostAttackerArmies)
        assertEquals(1, lostDefenderArmies)
    }

    @Test
    fun `Defender can't win more than the contested armies`() {
        val attackerRolls = listOf(1, 2)
        val defenderRolls = listOf(1, 5)
        val contestedArmies = Pos(1)

        val (lostAttackerArmies, lostDefenderArmies) =
            resolver.armiesLostForRolls(attackerRolls, defenderRolls, contestedArmies)

        assertEquals(1, lostAttackerArmies)
        assertEquals(0, lostDefenderArmies)
    }

    @Test
    fun `Can't contest more armies than the attacker's rolled dice`() {
        val contested = Pos(2)
        val exception = assertFailsWith<TooManyArmiesContestedException> {
            resolver.armiesLostForRolls(listOf(1), listOf(2, 3), contestedArmies = contested)
        }
        assertEquals(contested, exception.armies)
    }

    @Test
    fun `Can't contest more armies than the defender's rolled dice`() {
        val contested = Pos(3)
        val exception = assertFailsWith<TooManyArmiesContestedException> {
            resolver.armiesLostForRolls(listOf(1, 2, 3, 4), listOf(1), contestedArmies = contested)
        }
        assertEquals(contested, exception.armies)
    }
}

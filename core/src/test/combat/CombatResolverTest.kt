package combat

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CombatResolverTest {

    private lateinit var resolver: CombatResolver

    @BeforeTest
    fun setup() {
        resolver = CombatResolver()
    }

    @Test
    fun `Attacker wins when he rolls higher`() {
        val attackerRolls = listOf(6)
        val defenderRolls = listOf(1)

        val (lostAttackerArmies, lostDefenderArmies) =
            resolver.combat(attackerRolls, defenderRolls, contestedArmies = 1)

        assertEquals(0, lostAttackerArmies)
        assertEquals(1, lostDefenderArmies)
    }

    @Test
    fun `Defender wins when he rolls higher`() {
        val attackerRolls = listOf(1)
        val defenderRolls = listOf(6)

        val (lostAttackerArmies, lostDefenderArmies) =
            resolver.combat(attackerRolls, defenderRolls, contestedArmies = 1)

        assertEquals(1, lostAttackerArmies)
        assertEquals(0, lostDefenderArmies)
    }

    @Test
    fun `Defender wins when there's a tie`() {
        val attackerRolls = listOf(3)
        val defenderRolls = listOf(3)

        val (lostAttackerArmies, lostDefenderArmies) =
            resolver.combat(attackerRolls, defenderRolls, contestedArmies = 1)

        assertEquals(1, lostAttackerArmies)
        assertEquals(0, lostDefenderArmies)
    }

    @Test
    fun `Attacker can win more than one roll`() {
        val attackerRolls = listOf(6, 5)
        val defenderRolls = listOf(2, 1)

        val (lostAttackerArmies, lostDefenderArmies) =
            resolver.combat(attackerRolls, defenderRolls, contestedArmies = 2)

        assertEquals(0, lostAttackerArmies)
        assertEquals(2, lostDefenderArmies)
    }

    @Test
    fun `Attacker rolls are sorted before evaluating the results`() {
        val attackerRolls = listOf(1, 6)
        val defenderRolls = listOf(2)

        val (lostAttackerArmies, lostDefenderArmies) =
            resolver.combat(attackerRolls, defenderRolls, contestedArmies = 1)

        assertEquals(0, lostAttackerArmies)
        assertEquals(1, lostDefenderArmies)
    }

    @Test
    fun `Defender rolls are sorted before evaluating the results`() {
        val attackerRolls = listOf(6)
        val defenderRolls = listOf(2, 6)

        val (lostAttackerArmies, lostDefenderArmies) =
            resolver.combat(attackerRolls, defenderRolls, contestedArmies = 1)

        assertEquals(1, lostAttackerArmies)
        assertEquals(0, lostDefenderArmies)
    }

    @Test
    fun `Attacker can't win more than the contested armies`() {
        val attackerRolls = listOf(6, 3)
        val defenderRolls = listOf(1, 5)

        val (lostAttackerArmies, lostDefenderArmies) =
            resolver.combat(attackerRolls, defenderRolls, contestedArmies = 1)

        assertEquals(0, lostAttackerArmies)
        assertEquals(1, lostDefenderArmies)
    }

    @Test
    fun `Defender can't win more than the contested armies`() {
        val attackerRolls = listOf(1, 2)
        val defenderRolls = listOf(1, 5)

        val (lostAttackerArmies, lostDefenderArmies) =
            resolver.combat(attackerRolls, defenderRolls, contestedArmies = 1)

        assertEquals(1, lostAttackerArmies)
        assertEquals(0, lostDefenderArmies)
    }

    @Test
    fun `Can't contest more armies than the attacker's rolled dice`() {
        val contested = 2
        val exception = assertFailsWith<TooManyArmiesContestedException> {
            resolver.combat(listOf(1), listOf(2, 3), contestedArmies = contested)
        }
        assertEquals(contested, exception.armies)
    }

    @Test
    fun `Can't contest more armies than the defender's rolled dice`() {
        val contested = 3
        val exception = assertFailsWith<TooManyArmiesContestedException> {
            resolver.combat(listOf(1, 2, 3, 4), listOf(1), contestedArmies = contested)
        }
        assertEquals(contested, exception.armies)
    }

    @Test
    fun `Can't contest a negative amount of armies`() {
        val contested = -1
        val exception = assertFailsWith<NonPositiveArmiesContestedException> {
            resolver.combat(listOf(1), listOf(1), contestedArmies = contested)
        }
        assertEquals(contested, exception.armies)
    }

    @Test
    fun `Can't contest zero armies`() {
        val contested = 0
        val exception = assertFailsWith<NonPositiveArmiesContestedException> {
            resolver.combat(listOf(1), listOf(1), contestedArmies = contested)
        }
        assertEquals(contested, exception.armies)
    }
}

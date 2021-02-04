package gamelogic.combat

import Country
import gamelogic.combat.resolver.FixedCombatResolver
import gamelogic.map.NonExistentCountryException
import gamelogic.occupations.CountryOccupations
import gamelogic.occupations.Occupation
import gamelogic.occupations.TooManyArmiesRemovedException
import kotlin.test.*
import PositiveInt as Pos

class AttackerTest {
    @Test
    fun `Attacking removes one army from the attacker's occupation when the resolver says so`() {
        val attackTester = AttackTester(1, 0)

        val attack = attackTester.makeAttack()
        assertFalse(attack.isConquering)
        assertEquals(1, attack.armiesLostByAttacker)
        assertEquals(0, attack.armiesLostByDefender)

        attack.apply()

        attackTester.assertRightAmountOfArmiesWasRemoved()
    }

    @Test
    fun `Attacking removes many armies from the attacker's occupation when the resolver says so`() {
        val attackTester = AttackTester(3, 0)

        val attack = attackTester.makeAttack()
        assertFalse(attack.isConquering)
        assertEquals(3, attack.armiesLostByAttacker)
        assertEquals(0, attack.armiesLostByDefender)

        attack.apply()

        attackTester.assertRightAmountOfArmiesWasRemoved()
    }

    @Test
    fun `Attacking removes one army from the defender's occupation when the resolver says so`() {
        val attackTester = AttackTester(0, 1)

        val attack = attackTester.makeAttack()
        assertFalse(attack.isConquering)
        assertEquals(0, attack.armiesLostByAttacker)
        assertEquals(1, attack.armiesLostByDefender)

        attack.apply()

        attackTester.assertRightAmountOfArmiesWasRemoved()
    }

    @Test
    fun `Attacking removes many armies from the defender's occupation when the resolver says so`() {
        val attackTester = AttackTester(0, 2)

        val attack = attackTester.makeAttack()
        assertFalse(attack.isConquering)
        assertEquals(0, attack.armiesLostByAttacker)
        assertEquals(2, attack.armiesLostByDefender)

        attack.apply()

        attackTester.assertRightAmountOfArmiesWasRemoved()
    }

    @Test
    fun `Can't attack from a non-existent country`() {
        val attackTester = AttackTester(0, 2)

        val nonExistentCountry = "I don't exist"
        val exception = assertFailsWith<NonExistentCountryException> {
            attackTester.makeAttack(attackingCountry = nonExistentCountry)
        }
        assertEquals(nonExistentCountry, exception.country)
    }

    @Test
    fun `Can't attack a non-existent country`() {
        val attackTester = AttackTester(0, 2)

        val nonExistentCountry = "I don't exist"
        val exception = assertFailsWith<NonExistentCountryException> {
            attackTester.makeAttack(defendingCountry = nonExistentCountry)
        }
        assertEquals(nonExistentCountry, exception.country)
    }

    @Test
    fun `Can't apply without an amount of armies to move if the country is conquered`() {
        val defenderLostArmies = AttackTester.INITIAL_DEFENDER_ARMIES.toInt()
        val attackTester = AttackTester(0, defenderLostArmies)

        val attack = attackTester.makeAttack()
        assertTrue(attack.isConquering)
        assertEquals(0, attack.armiesLostByAttacker)
        assertEquals(defenderLostArmies, attack.armiesLostByDefender)

        assertFailsWith<NoArmiesProvidedWhenOccupyingException> {
            attack.apply()
        }
        attackTester.assertNothingChanged()
    }

    @Test
    fun `Can't apply with an amount of armies to move if not conquering`() {
        val attackTester = AttackTester(0, 2)

        val attack = attackTester.makeAttack()
        assertFalse(attack.isConquering)
        assertEquals(0, attack.armiesLostByAttacker)
        assertEquals(2, attack.armiesLostByDefender)

        assertFailsWith<ArmiesProvidedWhenNotOccupyingException> {
            attack.apply(Pos(2))
        }
        attackTester.assertNothingChanged()
    }

    @Test
    fun `Conquering moves the amount of armies passed`() {
        val defenderLostArmies = AttackTester.INITIAL_DEFENDER_ARMIES.toInt()
        val attackTester = AttackTester(0, defenderLostArmies)

        val attack = attackTester.makeAttack()
        assertTrue(attack.isConquering)
        assertEquals(0, attack.armiesLostByAttacker)
        assertEquals(defenderLostArmies, attack.armiesLostByDefender)

        val armiesToMove = Pos(2)
        attack.apply(armiesToMove)

        attackTester.assertDefendingCountryOccupiedByAttackerWith(armiesToMove)
    }

    @Test
    fun `Can't move more than 3 armies when conquering`() {
        val defenderLostArmies = AttackTester.INITIAL_DEFENDER_ARMIES.toInt()
        val attackTester = AttackTester(0, defenderLostArmies)

        val attack = attackTester.makeAttack()
        assertTrue(attack.isConquering)
        assertEquals(0, attack.armiesLostByAttacker)
        assertEquals(defenderLostArmies, attack.armiesLostByDefender)

        val armiesToMove = Pos(4)
        val exception = assertFailsWith<TooManyArmiesMovedException> {
            attack.apply(armiesToMove)
        }

        assertEquals(armiesToMove, exception.armies)
        attackTester.assertNothingChanged()
    }

    @Test
    fun `Can't move more armies than the remaining ones minus one`() {
        val defenderLostArmies = AttackTester.INITIAL_DEFENDER_ARMIES.toInt()
        val attackerLostArmies = 2
        val attackTester = AttackTester(attackerLostArmies, defenderLostArmies)

        val attack = attackTester.makeAttack()
        assertTrue(attack.isConquering)
        assertEquals(attackerLostArmies, attack.armiesLostByAttacker)
        assertEquals(defenderLostArmies, attack.armiesLostByDefender)
        val attackerRemainingArmies =
            AttackTester.INITIAL_ATTACKER_ARMIES - Pos(attackerLostArmies)
        assertEquals(attackerRemainingArmies, attack.attackerRemainingArmies)

        val exception = assertFailsWith<TooManyArmiesRemovedException> {
            attack.apply(attack.attackerRemainingArmies)
        }

        assertEquals(
            attack.attackerRemainingArmies + attackerLostArmies, exception.armies)
        attackTester.assertNothingChanged()
    }
}

class AttackTester(attackerLostArmies: Int, defenderLostArmies: Int) {

    private val countryOccupations = attackableCountryOccupations()
    private val expectedAttackerArmies =
        INITIAL_ATTACKER_ARMIES.toInt() - attackerLostArmies
    private val expectedDefenderArmies =
        INITIAL_DEFENDER_ARMIES.toInt() - defenderLostArmies
    private val combatResults =
        combatResultsWithLostArmies(Pair(attackerLostArmies, defenderLostArmies))
    private val attacker: Attacker

    init {
        val combatResolver = FixedCombatResolver(
            INITIAL_ATTACKER_ARMIES, INITIAL_DEFENDER_ARMIES, combatResults)
        attacker = Attacker(countryOccupations, combatResolver)
    }

    private fun attackableCountryOccupations(): CountryOccupations {
        val occupations = listOf(createAttackingOccupation(), createDefendingOccupation())
        return CountryOccupations(occupations)
    }

    private fun createAttackingOccupation() =
        Occupation(ATTACKING_COUNTRY, ATTACKING_PLAYER, INITIAL_ATTACKER_ARMIES)

    private fun createDefendingOccupation() =
        Occupation(DEFENDING_COUNTRY, DEFENDING_PLAYER, INITIAL_DEFENDER_ARMIES)

    private fun combatResultsWithLostArmies(lostArmies: Pair<Int, Int>) =
        CombatResults(lostArmies, Pair(listOf(1, 3, 4), listOf(2, 6)))

    fun makeAttack(
        attackingCountry: Country = ATTACKING_COUNTRY,
        defendingCountry: Country = DEFENDING_COUNTRY
    ): Attack = attacker.makeAttack(attackingCountry, defendingCountry)

    fun assertNothingChanged() {
        assertEquals(ATTACKING_PLAYER, countryOccupations.occupierOf(ATTACKING_COUNTRY))
        assertArmiesOfCountryAre(INITIAL_ATTACKER_ARMIES.toInt(), ATTACKING_COUNTRY)
        assertEquals(DEFENDING_PLAYER, countryOccupations.occupierOf(DEFENDING_COUNTRY))
        assertArmiesOfCountryAre(INITIAL_DEFENDER_ARMIES.toInt(), DEFENDING_COUNTRY)
    }

    fun assertRightAmountOfArmiesWasRemoved() {
        assertArmiesOfCountryAre(expectedAttackerArmies, ATTACKING_COUNTRY)
        assertArmiesOfCountryAre(expectedDefenderArmies, DEFENDING_COUNTRY)
    }

    fun assertDefendingCountryOccupiedByAttackerWith(armies: Pos) {
        assertArmiesOfCountryAre(
            expectedAttackerArmies - armies.toInt(), ATTACKING_COUNTRY)
        assertEquals(ATTACKING_PLAYER, countryOccupations.occupierOf(DEFENDING_COUNTRY))
        assertArmiesOfCountryAre(armies.toInt(), DEFENDING_COUNTRY)
    }

    private fun assertArmiesOfCountryAre(expected: Int, country: Country) =
        assertEquals(Pos(expected), armiesOf(country))

    private fun armiesOf(country: Country) = countryOccupations.armiesOf(country)

    companion object {
        const val ATTACKING_COUNTRY = "Argentina"
        const val DEFENDING_COUNTRY = "Uruguay"
        private const val ATTACKING_PLAYER = "Eric"
        private const val DEFENDING_PLAYER = "Nico"
        val INITIAL_ATTACKER_ARMIES = Pos(5)
        val INITIAL_DEFENDER_ARMIES = Pos(3)
    }
}

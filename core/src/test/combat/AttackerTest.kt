package combat

import Country
import combat.resolver.FixedCombatResolver
import map.NonExistentCountryException
import occupations.CountryOccupations
import occupations.Occupation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import PositiveInt as Pos

class AttackerTest {

    @Test
    fun `Attacking removes one army from the attacker's occupation when the resolver says so`() {
        val attackTester = AttackTester(1, 0)

        attackTester.attack()

        attackTester.assertRightAmountOfArmiesWasRemoved()
    }

    @Test
    fun `Attacking removes many armies from the attacker's occupation when the resolver says so`() {
        val attackTester = AttackTester(3, 0)

        attackTester.attack()

        attackTester.assertRightAmountOfArmiesWasRemoved()
    }

    @Test
    fun `Attacking removes one army from the defender's occupation when the resolver says so`() {
        val attackTester = AttackTester(0, 1)

        attackTester.attack()

        attackTester.assertRightAmountOfArmiesWasRemoved()
    }

    @Test
    fun `Attacking removes many armies from the defender's occupation when the resolver says so`() {
        val attackTester = AttackTester(0, 2)

        attackTester.attack()

        attackTester.assertRightAmountOfArmiesWasRemoved()
    }

    @Test
    fun `Can't attack from a non-existent country`() {
        val attackTester = AttackTester(0, 2)

        val nonExistentCountry = "I don't exist"
        val exception = assertFailsWith<NonExistentCountryException> {
            attackTester.attack(attackingCountry = nonExistentCountry)
        }
        assertEquals(nonExistentCountry, exception.country)
    }

    @Test
    fun `Can't attack to a non-existent country`() {
        val attackTester = AttackTester(0, 2)

        val nonExistentCountry = "I don't exist"
        val exception = assertFailsWith<NonExistentCountryException> {
            attackTester.attack(defendingCountry = nonExistentCountry)
        }
        assertEquals(nonExistentCountry, exception.country)
    }

    @Test
    fun `Removing all armies of the defender occupies the country with one army of the attacker`() {
        val attackTester = AttackTester(0, AttackTester.INITIAL_DEFENDER_ARMIES.toInt())

        attackTester.attack()

        attackTester.assertDefendingCountryOccupiedByAttackerWithOneArmy()
    }

    @Test
    fun `Occupying a country removes lost armies for the attacker as well as the occupying army`() {
        val attackTester = AttackTester(2, AttackTester.INITIAL_DEFENDER_ARMIES.toInt())

        attackTester.attack()

        attackTester.assertDefendingCountryOccupiedByAttackerWithOneArmy()
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

    fun attack(
        attackingCountry: Country = ATTACKING_COUNTRY,
        defendingCountry: Country = DEFENDING_COUNTRY
    ) = attacker.attack(attackingCountry, defendingCountry)

    fun assertRightAmountOfArmiesWasRemoved() {
        assertArmiesOfCountryAre(expectedAttackerArmies, ATTACKING_COUNTRY)
        assertArmiesOfCountryAre(expectedDefenderArmies, DEFENDING_COUNTRY)
    }

    fun assertDefendingCountryOccupiedByAttackerWithOneArmy() {
        assertArmiesOfCountryAre(expectedAttackerArmies - 1, ATTACKING_COUNTRY)
        assertEquals(ATTACKING_PLAYER, countryOccupations.occupierOf(DEFENDING_COUNTRY))
        assertArmiesOfCountryAre(1, DEFENDING_COUNTRY)
    }

    private fun assertArmiesOfCountryAre(expected: Int, country: Country) =
        assertEquals(Pos(expected), armiesOf(country))

    private fun armiesOf(country: Country) = countryOccupations.armiesOf(country)

    companion object {
        const val ATTACKING_COUNTRY = "Argentina"
        const val DEFENDING_COUNTRY = "Uruguay"
        private const val ATTACKING_PLAYER = "Eric"
        private const val DEFENDING_PLAYER = "Nico"
        val INITIAL_ATTACKER_ARMIES = Pos(4)
        val INITIAL_DEFENDER_ARMIES = Pos(3)
    }
}

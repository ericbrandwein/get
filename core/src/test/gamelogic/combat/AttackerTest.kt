package gamelogic.combat

import Country
import gamelogic.CountryReinforcement
import gamelogic.combat.resolver.FixedCombatResolver
import gamelogic.map.NonExistentCountryException
import gamelogic.occupations.CountryOccupations
import gamelogic.occupations.EmptyCountryException
import gamelogic.occupations.NoOccupation
import gamelogic.occupations.PlayerOccupation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import PositiveInt as Pos

class AttackerTest {
    @Test
    fun `Attacking removes one army from the attacker's occupation when the resolver says so`() {
        val attackTester = AttackTester(1, 0)

        attackTester.performAttack()

        attackTester.assertRightAmountOfArmiesWasRemoved()
    }

    @Test
    fun `Attacking removes many armies from the attacker's occupation when the resolver says so`() {
        val attackTester = AttackTester(3, 0)

        attackTester.performAttack()

        attackTester.assertRightAmountOfArmiesWasRemoved()
    }

    @Test
    fun `Attacking removes one army from the defender's occupation when the resolver says so`() {
        val attackTester = AttackTester(0, 1)

        attackTester.performAttack()

        attackTester.assertRightAmountOfArmiesWasRemoved()
    }

    @Test
    fun `Attacking removes many armies from the defender's occupation when the resolver says so`() {
        val attackTester = AttackTester(0, 2)

        attackTester.performAttack()

        attackTester.assertRightAmountOfArmiesWasRemoved()
    }

    @Test
    fun `Can't attack from a non-existent country`() {
        val attackTester = AttackTester(0, 2)

        val nonExistentCountry = "I don't exist"
        val exception = assertFailsWith<NonExistentCountryException> {
            attackTester.performAttack(attackingCountry = nonExistentCountry)
        }
        assertEquals(nonExistentCountry, exception.country)
    }

    @Test
    fun `Can't attack a non-existent country`() {
        val attackTester = AttackTester(0, 2)

        val nonExistentCountry = "I don't exist"
        val exception = assertFailsWith<NonExistentCountryException> {
            attackTester.performAttack(defendingCountry = nonExistentCountry)
        }
        assertEquals(nonExistentCountry, exception.country)
    }

    @Test
    fun `Performing an attack that removes all armies of the defender leaves the defender with zero armies`() {
        val defenderLostArmies = AttackTester.INITIAL_DEFENDER_ARMIES.toInt()
        val attackTester = AttackTester(0, defenderLostArmies)

        attackTester.performAttack()
        attackTester.assertRightAmountOfArmiesWasRemoved()
    }

    @Test
    fun `Can't attack from a non occupied country`() {
        val nonOccupiedCountry = "Argentina"
        val occupiedCountry = "Brasil"
        val occupations = listOf(
            NoOccupation(nonOccupiedCountry),
            PlayerOccupation(occupiedCountry, "Nico", Pos(2))
        )
        val countryOccupations = CountryOccupations(occupations)
        val combatResolver = FixedCombatResolver.createEmpty()
        val attacker = Attacker(countryOccupations, combatResolver)

        val exception = assertFailsWith<EmptyCountryException> {
            attacker.attack(nonOccupiedCountry, occupiedCountry)
        }

        assertEquals(nonOccupiedCountry, exception.country)
        assertEquals(0, countryOccupations.armiesOf(nonOccupiedCountry))
        assertEquals(2, countryOccupations.armiesOf(occupiedCountry))

    }

    @Test
    fun `Can't attack to a non occupied country`() {
        val nonOccupiedCountry = "Argentina"
        val occupiedCountry = "Brasil"
        val occupations = listOf(
            NoOccupation(nonOccupiedCountry),
            PlayerOccupation(occupiedCountry, "Nico", Pos(2))
        )
        val countryOccupations = CountryOccupations(occupations)
        val combatResolver = FixedCombatResolver.createEmpty()
        val attacker = Attacker(countryOccupations, combatResolver)

        val exception = assertFailsWith<EmptyCountryException> {
            attacker.attack(occupiedCountry, nonOccupiedCountry)
        }

        assertEquals(nonOccupiedCountry, exception.country)
        assertEquals(0, countryOccupations.armiesOf(nonOccupiedCountry))
        assertEquals(2, countryOccupations.armiesOf(occupiedCountry))
    }

    @Test
    fun `Attacking returns the combat results`() {
        val tester = AttackTester(1, 0)

        val combatResults = tester.performAttack()

        assertEquals(tester.combatResults, combatResults)
    }

    @Test
    fun `Cannot attack a country you own`() {
        val countries = listOf("Argentina", "Brasil")
        val occupations = countries.map { country ->
            PlayerOccupation(country, "Eric", Pos(2))
        }
        val countryOccupations = CountryOccupations(occupations)
        val combatResolver = FixedCombatResolver.createEmpty()
        val attacker = Attacker(countryOccupations, combatResolver)

        val exception = assertFailsWith<CannotAttackOwnCountryException> {
            attacker.attack(countries[0], countries[1])
        }

        assertEquals(countries[0], exception.from)
        assertEquals(countries[1], exception.to)
        assertEquals(2, countryOccupations.armiesOf(countries[0]))
        assertEquals(2, countryOccupations.armiesOf(countries[1]))
    }
}

class AttackTester(attackerLostArmies: Int, defenderLostArmies: Int) {

    private val countryOccupations = attackableCountryOccupations()
    private val expectedAttackerArmies =
        INITIAL_ATTACKER_ARMIES.toInt() - attackerLostArmies
    private val expectedDefenderArmies =
        INITIAL_DEFENDER_ARMIES.toInt() - defenderLostArmies
    val combatResults =
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
        PlayerOccupation(ATTACKING_COUNTRY, ATTACKING_PLAYER, INITIAL_ATTACKER_ARMIES)

    private fun createDefendingOccupation() =
        PlayerOccupation(DEFENDING_COUNTRY, DEFENDING_PLAYER, INITIAL_DEFENDER_ARMIES)

    private fun combatResultsWithLostArmies(lostArmies: Pair<Int, Int>) =
        CombatResults(lostArmies, Pair(listOf(1, 3, 4), listOf(2, 6)))

    fun performAttack(
        attackingCountry: Country = ATTACKING_COUNTRY,
        defendingCountry: Country = DEFENDING_COUNTRY
    ): CombatResults = attacker.attack(attackingCountry, defendingCountry)

    fun assertRightAmountOfArmiesWasRemoved() {
        assertArmiesOfCountryAre(expectedAttackerArmies, ATTACKING_COUNTRY)
        assertArmiesOfCountryAre(expectedDefenderArmies, DEFENDING_COUNTRY)
    }

    private fun assertArmiesOfCountryAre(expected: Int, country: Country) =
        assertEquals(expected, countryOccupations.armiesOf(country))

    companion object {
        const val ATTACKING_COUNTRY = "Argentina"
        const val DEFENDING_COUNTRY = "Uruguay"
        private const val ATTACKING_PLAYER = "Eric"
        private const val DEFENDING_PLAYER = "Nico"
        val INITIAL_ATTACKER_ARMIES = Pos(5)
        val INITIAL_DEFENDER_ARMIES = Pos(3)
    }
}

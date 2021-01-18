package combat

import Country
import combat.resolver.FixedCombatResolver
import map.NonExistentCountryException
import occupations.CountryOccupations
import occupations.Occupation
import occupations.TooManyArmiesRemovedException
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
    fun `Removing all armies of the defender asks the conqueror for the amount of armies to move`() {
        val attackTester = AttackTester(0, AttackTester.INITIAL_DEFENDER_ARMIES.toInt())

        attackTester.attack(conqueror = object : Conqueror {
            override fun armiesToMove(remainingAttackingArmies: Pos) =
                remainingAttackingArmies - Pos(2)
        })

        attackTester.assertDefendingCountryOccupiedByAttackerWith(
            AttackTester.INITIAL_ATTACKER_ARMIES - Pos(2))
    }

    @Test
    fun `Occupying a country removes lost armies for the attacker as well as the occupying army`() {
        val attackTester = AttackTester(2, AttackTester.INITIAL_DEFENDER_ARMIES.toInt())

        attackTester.attack(conqueror = SingleArmyConqueror())

        attackTester.assertDefendingCountryOccupiedByAttackerWith(Pos(1))
    }

    @Test
    fun `The conqueror can't move more armies than the remaining ones minus one`() {
        val attackTester = AttackTester(2, AttackTester.INITIAL_DEFENDER_ARMIES.toInt())

        val exception = assertFailsWith<TooManyArmiesRemovedException> {
            attackTester.attack(conqueror = object : Conqueror {
                override fun armiesToMove(remainingAttackingArmies: Pos) =
                    remainingAttackingArmies
            })
        }

        assertEquals(AttackTester.INITIAL_ATTACKER_ARMIES - Pos(2), exception.armies)
    }

    @Test
    fun `The conqueror can't move more than 3 armies`() {
        val attackTester = AttackTester(0, AttackTester.INITIAL_DEFENDER_ARMIES.toInt())

        val armiesMoved = Pos(4)
        val exception = assertFailsWith<TooManyArmiesMovedException> {
            attackTester.attack(conqueror = object : Conqueror {
                override fun armiesToMove(remainingAttackingArmies: Pos) = armiesMoved
            })
        }

        assertEquals(armiesMoved, exception.armies)
    }

    @Test
    fun `Attacking returns the results of combat`() {
        val attackerLostArmies = 0
        val defenderLostArmies = 2
        val attackTester = AttackTester(attackerLostArmies, defenderLostArmies)

        val results = attackTester.attack()

        assertEquals(attackTester.combatResults, results)
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
        Occupation(ATTACKING_COUNTRY, ATTACKING_PLAYER, INITIAL_ATTACKER_ARMIES)

    private fun createDefendingOccupation() =
        Occupation(DEFENDING_COUNTRY, DEFENDING_PLAYER, INITIAL_DEFENDER_ARMIES)

    private fun combatResultsWithLostArmies(lostArmies: Pair<Int, Int>) =
        CombatResults(lostArmies, Pair(listOf(1, 3, 4), listOf(2, 6)))

    fun attack(
        attackingCountry: Country = ATTACKING_COUNTRY,
        defendingCountry: Country = DEFENDING_COUNTRY,
        conqueror: Conqueror = SingleArmyConqueror()
    ): CombatResults = attacker.attack(attackingCountry, defendingCountry, conqueror)

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

package gamelogic.combat

import PositiveInt
import gamelogic.map.NonExistentCountryException
import gamelogic.occupations.CountryOccupations
import gamelogic.occupations.NoOccupation
import gamelogic.occupations.PlayerOccupation
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class OccupierTest {
    private val bigOccupierCountry = "Argentina"
    private val smallOccupierCountry = "Chile"
    private val occupiedCountry = "Brasil"
    private val nonExistentCountry = "asdasdsadsd"
    private val occupyingPlayer = "Eric"

    private val occupations = CountryOccupations(
        listOf(
            PlayerOccupation(bigOccupierCountry, occupyingPlayer, PositiveInt(5)),
            PlayerOccupation(smallOccupierCountry, occupyingPlayer, PositiveInt(2)),
            NoOccupation(occupiedCountry)
        ))

    @Test
    fun `Can't occupy from non existent country`() {
        val occupier = Occupier(occupations)

        val exception = assertFailsWith<NonExistentCountryException> {
            occupier.occupy(nonExistentCountry, occupiedCountry, PositiveInt(1))
        }

        assertEquals(nonExistentCountry, exception.country)
        assertEquals(0, occupations.armiesOf(occupiedCountry))
    }

    @Test
    fun `Can't occupy a non existent country`() {
        val occupier = Occupier(occupations)

        val exception = assertFailsWith<NonExistentCountryException> {
            occupier.occupy(bigOccupierCountry, nonExistentCountry, PositiveInt(1))
        }

        assertEquals(nonExistentCountry, exception.country)
        assertEquals(5, occupations.armiesOf(bigOccupierCountry))
    }

    @Test
    fun `Occupying an empty country changes the occupier and moves the requested armies`() {
        val occupier = Occupier(occupations)

        occupier.occupy(bigOccupierCountry, occupiedCountry, PositiveInt(2))

        assertFalse(occupations.isEmpty(occupiedCountry))
        assertEquals(occupyingPlayer, occupations.occupierOf(occupiedCountry))
        assertEquals(2, occupations.armiesOf(occupiedCountry))
        assertEquals(occupyingPlayer, occupations.occupierOf(bigOccupierCountry))
        assertEquals(3, occupations.armiesOf(bigOccupierCountry))
    }

    @Test
    fun `Can't occupy a country with more than the amount of armies in the occupying country minus one`() {
        val occupier = Occupier(occupations)

        val armies = PositiveInt(2)
        val exception = assertFailsWith<TooManyArmiesMovedException> {
            occupier.occupy(smallOccupierCountry, occupiedCountry, armies)
        }

        assertEquals(armies, exception.armies)
        assertEquals(2, occupations.armiesOf(smallOccupierCountry))
        assertTrue(occupations.isEmpty(occupiedCountry))
    }

    @Test
    fun `Can't occupy a country with more than three armies`() {
        val occupier = Occupier(occupations)

        val armies = PositiveInt(4)
        val exception = assertFailsWith<TooManyArmiesMovedException> {
            occupier.occupy(bigOccupierCountry, occupiedCountry, armies)
        }

        assertEquals(armies, exception.armies)
        assertEquals(5, occupations.armiesOf(bigOccupierCountry))
        assertTrue(occupations.isEmpty(occupiedCountry))
    }
}

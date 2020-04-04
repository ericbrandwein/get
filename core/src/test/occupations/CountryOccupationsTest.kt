package occupations

import Continent
import NonExistentCountryException
import kotlin.test.*

class CountryOccupationsTest {

    private val someCountry = "Argentina"
    private val otherCountry = "Brasil"
    private val nonExistentCountry = "Uruguay"
    private val someContinent = Continent("America", setOf(someCountry, otherCountry))
    private val someContinentSet = setOf(someContinent)
    private val somePlayer = "Popeye"
    private val otherPlayer = "Graciela"
    private val anotherPlayer = "Julieta"
    private val someArmies = 3
    private val otherArmies = 1
    private val anotherArmies = 2

    @Test
    fun `At first all countries are unoccupied`() {
        val occupations = CountryOccupations(someContinentSet)

        assertFalse(occupations.of(someCountry).isOccupied())
    }

    @Test
    fun `Can't ask for the occupation of a non-existent country`() {
        val occupations = CountryOccupations(someContinentSet)

        val exception = assertFailsWith<NonExistentCountryException> {
            occupations.of(nonExistentCountry)
        }

        assertEquals(nonExistentCountry, exception.country)
    }

    @Test
    fun `Occupying a country with one player makes it occupied by that player`() {
        val occupations = CountryOccupations(someContinentSet)

        occupations.occupy(someCountry, somePlayer, someArmies)

        val occupation = occupations.of(someCountry) as SinglePlayerOccupation
        assertTrue(occupation.isOccupied())
        assertEquals(somePlayer, occupation.occupier)
        assertEquals(someArmies, occupation.armies)
    }

    @Test
    fun `Occupying a country does not occupy all the others`() {
        val occupations = CountryOccupations(someContinentSet)

        occupations.occupy(someCountry, somePlayer, someArmies)

        val occupation = occupations.of(otherCountry)
        assertFalse(occupation.isOccupied())
    }

    @Test
    fun `Can't occupy a country already occupied by the same single player`() {
        val occupations = CountryOccupations(someContinentSet)

        occupations.occupy(someCountry, somePlayer, someArmies)

        val exception = assertFailsWith<PlayerAlreadyOccupiesCountryException> {
            occupations.occupy(someCountry, somePlayer, someArmies)
        }
        assertEquals(somePlayer, exception.player)
        val occupation = occupations.of(someCountry) as SinglePlayerOccupation
        assertTrue(occupation.isOccupied())
        assertEquals(somePlayer, occupation.occupier)
        assertEquals(someArmies, occupation.armies)
    }

    @Test
    fun `Can occupy a country occupied by other single player`() {
        val occupations = CountryOccupations(someContinentSet)

        occupations.occupy(someCountry, somePlayer, someArmies)
        occupations.occupy(someCountry, otherPlayer, otherArmies)

        val occupation = occupations.of(someCountry) as SinglePlayerOccupation
        assertEquals(otherPlayer, occupation.occupier)
        assertEquals(otherArmies, occupation.armies)
    }

    @Test
    fun `Can't occupy a non-existent country with a single player`() {
        val occupations = CountryOccupations(someContinentSet)

        val exception = assertFailsWith<NonExistentCountryException> {
            occupations.occupy(nonExistentCountry, somePlayer, someArmies)
        }

        assertEquals(nonExistentCountry, exception.country)
    }

    @Test
    fun `Occupying a country with two players makes it shared`() {
        val occupations = CountryOccupations(someContinentSet)

        occupations.occupy(someCountry, somePlayer, someArmies, otherPlayer, otherArmies)

        val occupation = occupations.of(someCountry) as SharedOccupation
        assertEquals(setOf(somePlayer, otherPlayer), occupation.occupiers)
        assertEquals(someArmies, occupation.armiesOf(somePlayer))
        assertEquals(otherArmies, occupation.armiesOf(otherPlayer))
    }

    @Test
    fun `Can't occupy a country with one player when he is one of the players occupying`() {
        val occupations = CountryOccupations(someContinentSet)

        occupations.occupy(someCountry, somePlayer, someArmies, otherPlayer, otherArmies)

        val exception = assertFailsWith<PlayerAlreadyOccupiesCountryException> {
            occupations.occupy(someCountry, somePlayer, anotherArmies)
        }

        assertEquals(somePlayer, exception.player)
        val occupation = occupations.of(someCountry) as SharedOccupation
        assertEquals(setOf(somePlayer, otherPlayer), occupation.occupiers)
        assertEquals(someArmies, occupation.armiesOf(somePlayer))
        assertEquals(otherArmies, occupation.armiesOf(otherPlayer))
    }

    @Test
    fun `Can't occupy a country with two players when the first one is already occupying the country`() {
        val occupations = CountryOccupations(someContinentSet)

        occupations.occupy(someCountry, somePlayer, someArmies, otherPlayer, otherArmies)
        val exception = assertFailsWith<PlayerAlreadyOccupiesCountryException> {
            occupations.occupy(someCountry, somePlayer, anotherArmies, anotherPlayer, someArmies)
        }

        assertEquals(somePlayer, exception.player)
        val occupation = occupations.of(someCountry) as SharedOccupation
        assertEquals(setOf(somePlayer, otherPlayer), occupation.occupiers)
        assertEquals(someArmies, occupation.armiesOf(somePlayer))
        assertEquals(otherArmies, occupation.armiesOf(otherPlayer))
    }

    @Test
    fun `Can't occupy a country with two players when the second one is already occupying the country`() {
        val occupations = CountryOccupations(someContinentSet)

        occupations.occupy(someCountry, somePlayer, someArmies)
        val exception = assertFailsWith<PlayerAlreadyOccupiesCountryException> {
            occupations.occupy(someCountry, otherPlayer, anotherArmies, somePlayer, someArmies)
        }

        assertEquals(somePlayer, exception.player)
        val occupation = occupations.of(someCountry) as SinglePlayerOccupation
        assertEquals(somePlayer, occupation.occupier)
        assertEquals(someArmies, occupation.armies)
    }

    @Test
    fun `Can't occupy a non-existent country with two players`() {
        val occupations = CountryOccupations(someContinentSet)

        val exception = assertFailsWith<NonExistentCountryException> {
            occupations.occupy(nonExistentCountry, somePlayer, someArmies, anotherPlayer, otherArmies)
        }

        assertEquals(nonExistentCountry, exception.country)
    }

    @Test
    fun `Removing a player from a shared occupation makes the other player the only occupier`() {
        val occupations = CountryOccupations(someContinentSet)

        occupations.occupy(someCountry, somePlayer, someArmies, otherPlayer, otherArmies)
        occupations.remove(someCountry, somePlayer)

        val occupation = occupations.of(someCountry) as SinglePlayerOccupation
        assertEquals(otherPlayer, occupation.occupier)
        assertEquals(otherArmies, occupation.armies)
    }

    @Test
    fun `Can't remove a player from a country occupied by only one player`() {
        val occupations = CountryOccupations(someContinentSet)

        occupations.occupy(someCountry, somePlayer, someArmies)

        val exception = assertFailsWith<CantRemoveOnlyOccupierException> {
            occupations.remove(someCountry, somePlayer)
        }

        assertEquals(somePlayer, exception.player)
        assertEquals(someCountry, exception.country)

        val occupation = occupations.of(someCountry) as SinglePlayerOccupation
        assertEquals(somePlayer, occupation.occupier)
        assertEquals(someArmies, occupation.armies)
    }

    @Test
    fun `Can't remove a player from a country not occupied by anyone`() {
        val occupations = CountryOccupations(someContinentSet)

        val exception = assertFailsWith<NotOccupyingPlayerException> {
            occupations.remove(someCountry, somePlayer)
        }

        assertEquals(somePlayer, exception.player)
        assertFalse(occupations.of(someCountry).isOccupied())
    }

    @Test
    fun `Can't remove a player that is not sharing country`() {
        val occupations = CountryOccupations(someContinentSet)

        occupations.occupy(someCountry, somePlayer, someArmies, otherPlayer, otherArmies)
        val exception = assertFailsWith<NotOccupyingPlayerException> {
            occupations.remove(someCountry, anotherPlayer)
        }

        assertEquals(anotherPlayer, exception.player)
        val occupation = occupations.of(someCountry) as SharedOccupation
        assertEquals(setOf(somePlayer, otherPlayer), occupation.occupiers)
        assertEquals(someArmies, occupation.armiesOf(somePlayer))
        assertEquals(otherArmies, occupation.armiesOf(otherPlayer))
    }
}

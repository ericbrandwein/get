package gamelogic.occupations

import PositiveInt
import gamelogic.map.NonExistentCountryException
import kotlin.test.*

class CountryOccupationsTest {
    @Test
    fun `Construction sets the occupiers of the countries`() {
        val country = "Argentina"
        val occupier = "Eric"
        val occupations = listOf(PlayerOccupation(country, occupier, PositiveInt(1)))
        val countryOccupations = CountryOccupations(occupations)

        assertEquals(occupier, countryOccupations.occupierOf(country))
    }

    @Test
    fun `Occupiers of two different countries are different if are set to be different`() {
        val countries = listOf("Argentina", "Uruguay")
        val occupiers = listOf("Eric", "Nico")
        val occupations = countries.zip(occupiers)
            .map { (country, occupier) -> PlayerOccupation(country, occupier, PositiveInt(1)) }
        val countryOccupations = CountryOccupations(occupations)

        assertEquals(occupiers[0], countryOccupations.occupierOf(countries[0]))
        assertEquals(occupiers[1], countryOccupations.occupierOf(countries[1]))
    }

    @Test
    fun `Can't ask occupier of non-existent country`() {
        val countryOccupations = CountryOccupations(listOf())
        val country = "Argentina"
        val exception = assertFailsWith<NonExistentCountryException> {
            countryOccupations.occupierOf(country)
        }

        assertEquals(country, exception.country)
    }

    @Test
    fun `Construction sets the armies of the countries`() {
        val country = "Argentina"
        val armies = PositiveInt(1)
        val occupations = listOf(PlayerOccupation(country, "Eric", armies))
        val countryOccupations = CountryOccupations(occupations)

        assertEquals(armies.toInt(), countryOccupations.armiesOf(country))
    }
    
    @Test
    fun `Can't occupy a country without removing all armies of the other player`() {
        val country = "Argentina"
        val oldPlayer = "Eric"
        val armies = PositiveInt(1)
        val occupations = listOf(PlayerOccupation(country, oldPlayer, armies))
        val countryOccupations = CountryOccupations(occupations)

        val exception = assertFailsWith<OccupiedCountryException> {
            countryOccupations.occupy(country, "Nico", PositiveInt(2))
        }
        assertEquals(country, exception.country)
        assertEquals(oldPlayer, countryOccupations.occupierOf(country))
        assertEquals(armies.toInt(), countryOccupations.armiesOf(country))
    }

    @Test
    fun `Can occupy a country after removing all armies of the other player`() {
        val country = "Argentina"
        val oldPlayer = "Eric"
        val armies = PositiveInt(1)
        val occupations = listOf(PlayerOccupation(country, oldPlayer, armies))
        val countryOccupations = CountryOccupations(occupations)

        val newPlayer = "Nico"
        val newArmies = PositiveInt(2)

        countryOccupations.removeArmies(country, PositiveInt(1))
        countryOccupations.occupy(country, newPlayer, newArmies)

        assertEquals(newPlayer, countryOccupations.occupierOf(country))
        assertEquals(newArmies.toInt(), countryOccupations.armiesOf(country))
    }

    @Test
    fun `Can't occupy a non-existent country`() {
        val countryOccupations = CountryOccupations(listOf())

        val nonExistentCountry = "Brazil"
        val exception = assertFailsWith<NonExistentCountryException> {
            countryOccupations.occupy(nonExistentCountry, "Eric", PositiveInt(1))
        }

        assertEquals(nonExistentCountry, exception.country)
    }

    @Test
    fun `Can add armies to occupation`() {
        val country = "Argentina"
        val armies = PositiveInt(1)
        val occupations = listOf(PlayerOccupation(country, "Eric", armies))
        val countryOccupations = CountryOccupations(occupations)

        val added = PositiveInt(2)
        countryOccupations.addArmies(country, added)

        assertEquals((armies + added).toInt(), countryOccupations.armiesOf(country))
    }

    @Test
    fun `Can remove armies from occupation`() {
        val country = "Argentina"
        val armies = PositiveInt(3)
        val occupations = listOf(PlayerOccupation(country, "Jose", armies))
        val countryOccupations = CountryOccupations(occupations)

        val removed = PositiveInt(2)
        countryOccupations.removeArmies(country, removed)

        assertEquals((armies - removed).toInt(), countryOccupations.armiesOf(country))
    }

    @Test
    fun `Removing all armies from an Occupation removes the Occupation`() {
        val country = "Argentina"
        val armies = PositiveInt(3)
        val occupations = listOf(PlayerOccupation(country, "Nico", armies))
        val countryOccupations = CountryOccupations(occupations)

        countryOccupations.removeArmies(country, armies)

        assertEquals(0, countryOccupations.armiesOf(country))
    }

    @Test
    fun `Can't remove more armies than there are in the country`() {
        val country = "Argentina"
        val armies = PositiveInt(2)
        val occupations = listOf(PlayerOccupation(country, "Nico", armies))
        val countryOccupations = CountryOccupations(occupations)

        assertFailsWith<TooManyArmiesRemovedException> {
            countryOccupations.removeArmies(country, PositiveInt(3))
        }

        assertEquals(2, countryOccupations.armiesOf(country))
    }

    @Test
    fun `Occupied country is not empty`() {
        val country = "Argneiojaia"
        val occupations = listOf(PlayerOccupation(country, "Nico", PositiveInt(1)))
        val countryOccupations = CountryOccupations(occupations)

        assertFalse(countryOccupations.isEmpty(country))
    }

    @Test
    fun `Country is empty after removing all armies`() {
        val country = "Argneiojaia"
        val armies = PositiveInt(1)
        val occupations = listOf(PlayerOccupation(country, "Nico", armies))
        val countryOccupations = CountryOccupations(occupations)

        countryOccupations.removeArmies(country, armies)

        assertTrue(countryOccupations.isEmpty(country))
    }

    @Test
    fun `Can't ask if a non existent country is empty`() {
        val occupations = listOf<Occupation>()
        val countryOccupations = CountryOccupations(occupations)
        val country = "Argneiojaia"

        val exception = assertFailsWith<NonExistentCountryException> {
            countryOccupations.isEmpty(country)
        }

        assertEquals(country, exception.country)
    }

    @Test
    fun `Can't ask occupier of an empty country`() {
        val country = "Argentina"
        val occupations = listOf(NoOccupation(country))
        val countryOccupations = CountryOccupations(occupations)

        val exception = assertFailsWith<EmptyCountryException> {
            countryOccupations.occupierOf(country)
        }

        assertEquals(country, exception.country)
        assertTrue(countryOccupations.isEmpty(country))
    }

    @Test
    fun `Can't add armies to an empty country`() {
        val country = "Argentina"
        val occupations = listOf(NoOccupation(country))
        val countryOccupations = CountryOccupations(occupations)

        val exception = assertFailsWith<EmptyCountryException> {
            countryOccupations.addArmies(country, PositiveInt(1))
        }

        assertEquals(country, exception.country)
        assertTrue(countryOccupations.isEmpty(country))
    }

    @Test
    fun `Can't remove armies from an empty country`() {
        val country = "Argentina"
        val occupations = listOf(NoOccupation(country))
        val countryOccupations = CountryOccupations(occupations)

        val exception = assertFailsWith<EmptyCountryException> {
            countryOccupations.removeArmies(country, PositiveInt(1))
        }

        assertEquals(country, exception.country)
        assertTrue(countryOccupations.isEmpty(country))
    }
}

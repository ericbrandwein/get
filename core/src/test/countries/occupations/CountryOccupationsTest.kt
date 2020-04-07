package countries.occupations

import countries.NonExistentCountryException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CountryOccupationsTest {
    @Test
    fun `Construction sets the occupiers of the countries`() {
        val country = "Argentina"
        val occupier = "Eric"
        val occupations = listOf(Occupation(country, occupier, 1))
        val countryOccupations = CountryOccupations(occupations)

        assertEquals(occupier, countryOccupations.occupierOf(country))
    }

    @Test
    fun `Occupiers of two different countries are different if are set to be different`() {
        val countries = listOf("Argentina", "Uruguay")
        val occupiers = listOf("Eric", "Nico")
        val occupations = countries.zip(occupiers)
            .map { (country, occupier) -> Occupation(country, occupier, 1) }
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
        val armies = 1
        val occupations = listOf(Occupation(country, "Eric", armies))
        val countryOccupations = CountryOccupations(occupations)

        assertEquals(armies, countryOccupations.armiesOf(country))
    }

    @Test
    fun `Can occupy a country with another player and armies`() {
        val country = "Argentina"
        val occupations = listOf(Occupation(country, "Eric", 1))
        val countryOccupations = CountryOccupations(occupations)

        val newPlayer = "Nico"
        val newArmies = 2
        countryOccupations.occupy(country, newPlayer, newArmies)

        assertEquals(newPlayer, countryOccupations.occupierOf(country))
        assertEquals(newArmies, countryOccupations.armiesOf(country))
    }

    @Test
    fun `Can't occupy a non-existent country`() {
        val countryOccupations = CountryOccupations(listOf())

        val nonExistentCountry = "Brazil"
        val exception = assertFailsWith<NonExistentCountryException> {
            countryOccupations.occupy(nonExistentCountry, "Eric", 1)
        }

        assertEquals(nonExistentCountry, exception.country)
    }

    @Test
    fun `Can add armies to occupation`() {
        val country = "Argentina"
        val armies = 1
        val occupations = listOf(Occupation(country, "Eric", armies))
        val countryOccupations = CountryOccupations(occupations)

        val added = 2
        countryOccupations.addArmies(country, added)

        assertEquals(armies + added, countryOccupations.armiesOf(country))
    }

    @Test
    fun `Can remove armies from occupation`() {
        val country = "Argentina"
        val armies = 3
        val occupations = listOf(Occupation(country, "Jose", armies))
        val countryOccupations = CountryOccupations(occupations)

        val removed = 2
        countryOccupations.removeArmies(country, removed)

        assertEquals(armies - removed, countryOccupations.armiesOf(country))
    }
}

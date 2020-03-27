import kotlin.test.*

class PoliticalMapTest {
    @Test
    fun `Builder with no countries creates a Map with no countries`() {
        val builder = PoliticalMap.Builder()
        val map = builder.build()

        assertEquals(0, map.countries.size)
    }

    @Test
    fun `Builder with a country adds that country to the Map`() {
        val country = "Argentina"
        val continent = "America"

        val builder = PoliticalMap.Builder()
        builder.addCountry(country, continent)
        val map = builder.build()

        val countries = map.countries
        assertEquals(1, countries.size)
        assertEquals(country, countries.single())
        assertEquals(continent, map.continentOf(country))
        assertEquals(setOf(country), map.countriesOf(continent))
    }

    @Test
    fun `Can't ask continent of a non existent country`() {
        val builder = PoliticalMap.Builder()
        val map = builder.build()
        val country = "Argentina"

        val exception = assertFailsWith<NonExistentCountryException> {
            map.continentOf(country)
        }
        assertEquals(country, exception.country)
    }

    @Test
    fun `Can't ask countries of a non existent continent`() {
        val builder = PoliticalMap.Builder()
        val map = builder.build()
        val continent = "America"

        val exception = assertFailsWith<NonExistentContinentException> {
            map.countriesOf(continent)
        }
        assertEquals(continent, exception.continent)
    }

    @Test
    fun `Builder with two countries adds both to the countries list`() {
        val firstCountry = "Argentina"
        val secondCountry = "Uruguay"
        val continent = "America"

        val map = PoliticalMap.Builder()
            .addCountry(firstCountry, continent)
            .addCountry(secondCountry, continent)
            .build()

        assertEquals(setOf(firstCountry, secondCountry), map.countries)
    }

    @Test
    fun `Map with two countries with same continent returns same continent for both`() {
        val firstCountry = "Argentina"
        val secondCountry = "Uruguay"
        val continent = "America"
        val builder = PoliticalMap.Builder()
        builder.addCountry(firstCountry, continent)
        builder.addCountry(secondCountry, continent)
        val map = builder.build()

        assertEquals(continent, map.continentOf(firstCountry))
        assertEquals(continent, map.continentOf(secondCountry))
    }

    @Test
    fun `Map with two countries with different continents returns the corresponding continent for both of them`() {
        val firstCountry = "Argentina"
        val firstContinent = "America"
        val secondCountry = "China"
        val secondContinent = "Asia"

        val map = PoliticalMap.Builder()
            .addCountry(firstCountry, firstContinent)
            .addCountry(secondCountry, secondContinent)
            .build()

        assertEquals(firstContinent, map.continentOf(firstCountry))
        assertEquals(secondContinent, map.continentOf(secondCountry))
    }

    @Test
    fun `Can't ask the continent of a non existent country when there are other countries`() {
        val map = PoliticalMap.Builder()
            .addCountry("Argentina", "America")
            .build()

        val nonExistentCountry = "India"
        val exception = assertFailsWith<NonExistentCountryException> {
            map.continentOf(nonExistentCountry)
        }
        assertEquals(nonExistentCountry, exception.country)
    }

    @Test
    fun `Can't add two times the same country`() {
        val country = "Argentina"
        val builder = PoliticalMap.Builder().addCountry(country, "America")

        val exception = assertFailsWith<CountryAlreadyExistsException> {
            builder.addCountry(country, "Europa")
        }
        assertEquals(country, exception.country)
    }

    @Test
    fun `Can't ask the countries of a continent that does not exist when there are other continents`() {
        val map = PoliticalMap.Builder()
            .addCountry("Argentina", "America")
            .build()

        val nonExistentContinent = "Europa"
        val exception = assertFailsWith<NonExistentContinentException> {
            map.countriesOf(nonExistentContinent)
        }
        assertEquals(nonExistentContinent, exception.continent)
    }

    @Test
    fun `Countries of a continent when there are many continents returns only the countries of that continent`() {
        val firstContinent = "America"
        val firstCountry = "Argentina"
        val secondContinent = "Africa"
        val secondCountry = "South Africa"
        val map = PoliticalMap.Builder()
            .addCountry(firstCountry, firstContinent)
            .addCountry(secondCountry, secondContinent)
            .build()

        assertEquals(setOf(firstCountry), map.countriesOf(firstContinent))
        assertEquals(setOf(secondCountry), map.countriesOf(secondContinent))
    }

    @Test
    fun `Country with no borders added doesn't border any other country`() {
        val continent = "America"
        val country = "Argentina"
        val otherCountry = "Uruguay"
        val map = PoliticalMap.Builder()
            .addCountry(country, continent)
            .addCountry(otherCountry, continent)
            .build()

        assertFalse(map.areBordering(country, otherCountry))
    }

    @Test
    fun `Two countries added as bordering in Builder are bordering in Map`() {
        val continent = "America"
        val country = "Argentina"
        val otherCountry = "Uruguay"
        val map = PoliticalMap.Builder()
            .addCountry(country, continent)
            .addCountry(otherCountry, continent)
            .addBorder(country, otherCountry)
            .build()

        assertTrue(map.areBordering(country, otherCountry))
    }

    @Test
    fun `Country added as border of other country borders other country`() {
        val continent = "America"
        val country = "Argentina"
        val otherCountry = "Uruguay"
        val map = PoliticalMap.Builder()
            .addCountry(country, continent)
            .addCountry(otherCountry, continent)
            .addBorder(country, otherCountry)
            .build()

        assertTrue(map.areBordering(otherCountry, country))
    }

    @Test
    fun `Can't add borders to non existent second country`() {
        val continent = "America"
        val country = "Argentina"
        val builder = PoliticalMap.Builder()
            .addCountry(country, continent)

        val nonExistentCountry = "I don't exist"
        val exception = assertFailsWith<NonExistentCountryException> {
            builder.addBorder(country, nonExistentCountry)
        }

        assertEquals(nonExistentCountry, exception.country)
    }

    @Test
    fun `Can't add borders to non existent first country`() {
        val continent = "America"
        val country = "Argentina"
        val builder = PoliticalMap.Builder()
            .addCountry(country, continent)

        val nonExistentCountry = "I don't exist"
        val exception = assertFailsWith<NonExistentCountryException> {
            builder.addBorder(nonExistentCountry, country)
        }
        assertEquals(nonExistentCountry, exception.country)
    }

    @Test
    fun `Can't check borders of non existent country`() {
        val map = PoliticalMap.Builder().build()

        val nonExistentCountry = "Argentina"
        val exception = assertFailsWith<NonExistentCountryException> {
            map.areBordering(nonExistentCountry, nonExistentCountry)
        }
        assertEquals(nonExistentCountry, exception.country)
    }

    @Test
    fun `Can't check if country borders a non existent country`() {
        val country = "Argentina"
        val map = PoliticalMap.Builder()
            .addCountry(country, "America")
            .build()

        val nonExistentCountry = "Uruguay"
        val exception = assertFailsWith<NonExistentCountryException> {
            map.areBordering(country, nonExistentCountry)
        }
        assertEquals(nonExistentCountry, exception.country)
    }
}
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
        val continent = Continent("America", setOf(country))
        val builder = PoliticalMap.Builder()
        builder.addContinent(continent)
        val map = builder.build()

        val countries = map.countries
        assertEquals(1, countries.size)
        assertEquals(country, countries.single())
        assertEquals(continent, map.continentOf(country))
        assertEquals(setOf(country), continent.countries)
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
    fun `Builder with two countries adds both to the countries list`() {
        val firstCountry = "Argentina"
        val secondCountry = "Uruguay"
        val continent = Continent("America", setOf(firstCountry, secondCountry))

        val map = PoliticalMap.Builder()
                .addContinent(continent)
                .build()

        assertEquals(setOf(firstCountry, secondCountry), map.countries)
    }

    @Test
    fun `Map with two countries with same continent returns same continent for both`() {
        val firstCountry = "Argentina"
        val secondCountry = "Uruguay"
        val continent = Continent("America", setOf(firstCountry, secondCountry))
        val builder = PoliticalMap.Builder()
        builder.addContinent(continent)
        val map = builder.build()

        assertEquals(continent, map.continentOf(firstCountry))
        assertEquals(continent, map.continentOf(secondCountry))
    }

    @Test
    fun `Map with two countries with different continents returns the corresponding continent for both of them`() {
        val firstCountry = "Argentina"
        val firstContinent = Continent("America", setOf(firstCountry))
        val secondCountry = "China"
        val secondContinent = Continent("Asia", setOf(secondCountry))

        val map = PoliticalMap.Builder()
                .addContinent(firstContinent)
                .addContinent(secondContinent)
                .build()

        assertEquals(firstContinent, map.continentOf(firstCountry))
        assertEquals(secondContinent, map.continentOf(secondCountry))
    }

    @Test
    fun `Can't ask the continent of a non existent country when there are other countries`() {
        val continent = Continent("America", setOf("Argentina"))
        val map = PoliticalMap.Builder()
                .addContinent(continent)
                .build()

        val nonExistentCountry = "India"
        val exception = assertFailsWith<NonExistentCountryException> {
            map.continentOf(nonExistentCountry)
        }
        assertEquals(nonExistentCountry, exception.country)
    }


    @Test
    fun `Country with no borders added doesn't border any other country`() {
        val country = "Argentina"
        val otherCountry = "Uruguay"
        val continent = Continent("America", setOf(country, otherCountry))
        val map = PoliticalMap.Builder()
                .addContinent(continent)
                .build()

        assertFalse(map.areBordering(country, otherCountry))
    }

    @Test
    fun `Two countries added as bordering in Builder are bordering in Map`() {
        val country = "Argentina"
        val otherCountry = "Uruguay"
        val continent = Continent("America", setOf(country, otherCountry))

        val map = PoliticalMap.Builder()
                .addContinent(continent)
                .addBorder(country, otherCountry)
                .build()

        assertTrue(map.areBordering(country, otherCountry))
    }

    @Test
    fun `Country added as border of other country borders other country`() {
        val country = "Argentina"
        val otherCountry = "Uruguay"
        val continent = Continent("America", setOf(country,otherCountry))
        val map = PoliticalMap.Builder()
                .addContinent(continent)
                .addBorder(country, otherCountry)
                .build()

        assertTrue(map.areBordering(otherCountry, country))
    }

    @Test
    fun `Can't add borders to non existent second country`() {
        val country = "Argentina"
        val continent = Continent("America", setOf(country))

        val builder = PoliticalMap.Builder()
                .addContinent(continent)

        val nonExistentCountry = "I don't exist"
        val exception = assertFailsWith<NonExistentCountryException> {
            builder.addBorder(country, nonExistentCountry)
        }

        assertEquals(nonExistentCountry, exception.country)
    }

    @Test
    fun `Can't add borders to non existent first country`() {
        val country = "Argentina"
        val continent = Continent("America", setOf(country))
        val builder = PoliticalMap.Builder()
                .addContinent(continent)

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
        val continent = Continent("America", setOf(country))
        val map = PoliticalMap.Builder()
                .addContinent(continent)
                .build()

        val nonExistentCountry = "Uruguay"
        val exception = assertFailsWith<NonExistentCountryException> {
            map.areBordering(country, nonExistentCountry)
        }
        assertEquals(nonExistentCountry, exception.country)
    }

    @Test
    fun `Can't add an already existing country in other continent`() {
        val country = "Argentina"
        val continent = Continent("America", setOf(country))
        val otherContinent = Continent("SouthAmerica", setOf(country))
        val map = PoliticalMap.Builder().addContinent(continent)

        val exception = assertFailsWith<CountryAlreadyExistsException> {
            map.addContinent(otherContinent)
        }
        assertEquals(country, exception.country)

    }

    @Test
    fun `Map with no countries has no continents`() {
        val map = PoliticalMap.Builder()
                .build()

        assertTrue(map.continents.isEmpty())
    }

    @Test
    fun `Map with one country has its continent`() {
        val country = "Guyana"
        val continent = Continent("America", setOf(country))
        val map = PoliticalMap.Builder()
                .addContinent(continent)
                .build()

        val continents = map.continents
        assertEquals(1, continents.size)
        assertEquals(continent, continents.single())
    }

    @Test
    fun `Map with two countries with same continent has their continent once`() {
        val continent = Continent("America", setOf("Guyana", "Peru"))
        val map = PoliticalMap.Builder()
                .addContinent(continent)
                .build()

        val continents = map.continents
        assertEquals(1, continents.size)
        assertEquals(continent, continents.single())
    }

    @Test
    fun `Map with two countries with different continents has each continent`() {
        val firstContinent = Continent("America", setOf<Country>())
        val secondContinent = Continent("Asia", setOf<Country>())
        val map = PoliticalMap.Builder()
                .addContinent(firstContinent)
                .addContinent(secondContinent)
                .build()

        val continents = map.continents
        assertEquals(2, continents.size)
        assertTrue(continents.contains(firstContinent))
        assertTrue(continents.contains(secondContinent))
    }



}


class ContinentTest {

    @Test
    fun `Continent with a country must contain it`() {
        val country = "Argentina"
        val continent = Continent("America", setOf(country))

        assertTrue(continent.containsCountry(country))
    }

    @Test
    fun `Continent without a country must not contain it`() {
        val country = "Argentina"
        val continent = Continent("America", setOf(country))
        val otherCountry = "Brasil"
        assertTrue(!continent.containsCountry(otherCountry))
    }

    @Test
    fun `Countries of a continent when there are many continents returns only the countries of that continent`() {
        val firstCountry = "Argentina"
        val firstContinent = Continent("America", setOf(firstCountry))
        val secondCountry = "South Africa"
        val secondContinent = Continent("Africa", setOf(secondCountry))

        assertEquals(setOf(firstCountry), firstContinent.countries)
        assertEquals(setOf(secondCountry), secondContinent.countries)
    }
}


class ContinentSetTest {

    @Test
    fun `ContinentSet must contain its coutries`() {
        val country =  "Argentina"
        val continent = Continent("America", setOf(country))
        val continentSet = ContinentSet()
        continentSet.add(continent)

        assertTrue(continentSet.countries.contains(country))
    }


    @Test
    fun `ContinentSet must contain the continent of a country included`() {
        val country =  "Argentina"
        val continent = Continent("America", setOf(country))
        val continentSet = ContinentSet()
        continentSet.add(continent)

        assertTrue(continentSet.anyHasCountry(country))
        assertEquals(continentSet.forCountry(country), continent)
        assertTrue(continentSet.doesContinentExist(continent))
    }


    @Test
    fun `ContinentSet must not contain a non included country`() {
        val country =  "Argentina"
        val continent = Continent("America", setOf(country))
        val continentSet = ContinentSet()
        continentSet.add(continent)
        val otherCountry = "Brasil"

        val exception = assertFailsWith<NonExistentCountryException> {
            continentSet.forCountry(otherCountry)
        }
        assertEquals(otherCountry, exception.country)
    }


}
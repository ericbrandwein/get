typealias Country = String

class PoliticalMap private constructor(
    private val continents: MutableSet<Continent>,
    private val borders: Map<Country, Collection<Country>>
) {

    val countries get() = continents.flatMap { continent -> continent.countries }.toSet()

    fun continentOf(country: Country): String {
        assertCountryExists(country)
        return continents.first { it.containsCountry(country) }.name
    }

    private fun assertCountryExists(country: Country) {
        if (!doesCountryExist(country)) {
            throw NonExistentCountryException(country)
        }
    }

    private fun doesCountryExist(country: Country) =
        continents.any { it.containsCountry(country) }

    fun countriesOf(continent: String): Collection<Country> {
        assertContinentExists(continent)
        return getContinent(continent).countries
    }

    private fun assertContinentExists(continent: String) {
        if (!doesContinentExist(continent)) {
            throw NonExistentContinentException(continent)
        }
    }

    private fun doesContinentExist(continent: String) =
        continents.any { it.name == continent }

    private fun getContinent(name: String) = continents.first { it.name == name }

    fun areBordering(firstCountry: Country, secondCountry: Country): Boolean {
        assertCountryExists(firstCountry)
        assertCountryExists(secondCountry)
        return borders.getValue(firstCountry).contains(secondCountry)
    }

    class Builder {

        private val continents = mutableSetOf<Continent>()
        private val borders = mutableMapOf<Country, MutableCollection<Country>>()

        fun addCountry(country: Country, continent: String): Builder {
            assertCountryDoesNotExist(country)
            getOrPutContinent(continent).addCountry(country)
            borders[country] = mutableSetOf()
            return this
        }

        private fun getOrPutContinent(name: String) =
            getContinent(name) ?: createContinent(name)

        private fun getContinent(name: String) =
            continents.firstOrNull { it.name == name }

        private fun createContinent(name: String): Continent {
            val continent = Continent(name)
            continents.add(continent)
            return continent
        }

        private fun assertCountryDoesNotExist(country: Country) {
            if (doesCountryExist(country)) {
                throw CountryAlreadyExistsException(country)
            }
        }

        private fun doesCountryExist(country: Country) =
            continents.any { it.containsCountry(country) }

        fun addBorder(firstCountry: Country, secondCountry: Country): Builder {
            assertCountryExists(firstCountry)
            assertCountryExists(secondCountry)
            borders.getValue(firstCountry).add(secondCountry)
            borders.getValue(secondCountry).add(firstCountry)
            return this
        }

        private fun assertCountryExists(country: Country) {
            if (!doesCountryExist(country)) {
                throw NonExistentCountryException(country)
            }
        }

        fun build() = PoliticalMap(continents, borders)
    }
}

private class Continent(val name: String) {
    val countries = mutableSetOf<String>()

    fun addCountry(country: Country) = countries.add(country)
    fun containsCountry(country: Country) = countries.contains(country)
}
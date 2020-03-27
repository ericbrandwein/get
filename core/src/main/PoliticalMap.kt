class PoliticalMap private constructor(
    private val continents: MutableSet<Continent>,
    private val borders: Map<String, Collection<String>>
) {

    val countries get() = continents.flatMap { continent -> continent.countries }.toSet()

    fun continentOf(country: String): String {
        assertCountryExists(country)
        return continents.first { it.containsCountry(country) }.name
    }

    private fun assertCountryExists(country: String) {
        if (!doesCountryExist(country)) {
            throw NonExistentCountryException(country)
        }
    }

    private fun doesCountryExist(country: String) =
        continents.any { it.containsCountry(country) }

    fun countriesOf(continent: String): Collection<String> {
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

    fun areBordering(firstCountry: String, secondCountry: String): Boolean {
        assertCountryExists(firstCountry)
        assertCountryExists(secondCountry)
        return borders.getValue(firstCountry).contains(secondCountry)
    }

    class Builder {

        private val continents = mutableSetOf<Continent>()
        private val borders = mutableMapOf<String, MutableCollection<String>>()

        fun addCountry(country: String, continent: String): Builder {
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

        private fun assertCountryDoesNotExist(country: String) {
            if (doesCountryExist(country)) {
                throw CountryAlreadyExistsException(country)
            }
        }

        private fun doesCountryExist(country: String) =
            continents.any { it.containsCountry(country) }

        fun addBorder(firstCountry: String, secondCountry: String): Builder {
            assertCountryExists(firstCountry)
            assertCountryExists(secondCountry)
            borders.getValue(firstCountry).add(secondCountry)
            borders.getValue(secondCountry).add(firstCountry)
            return this
        }

        private fun assertCountryExists(country: String) {
            if (!doesCountryExist(country)) {
                throw NonExistentCountryException(country)
            }
        }

        fun build() = PoliticalMap(continents, borders)
    }
}

private class Continent(val name: String) {
    val countries = mutableSetOf<String>()

    fun addCountry(country: String) = countries.add(country)
    fun containsCountry(country: String) = countries.contains(country)
}
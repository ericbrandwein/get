class PoliticalMap private constructor(
    private val continents: Map<String, Collection<String>>,
    private val borders: Map<String, Collection<String>>
) {

    val countries get() = continents.flatMap { (_, countries) -> countries }.toSet()

    fun continentOf(country: String): String {
        assertCountryExists(country)
        return continents.keys.first { continents.getValue(it).contains(country) }
    }

    private fun assertCountryExists(country: String) {
        if (!continents.values.any { it.contains(country) }) {
            throw NonExistentCountryException(country)
        }
    }

    fun countriesOf(continent: String): Collection<String> {
        assertContinentExists(continent)
        return continents.getValue(continent)
    }

    private fun assertContinentExists(continent: String) {
        if (!continents.containsKey(continent)) {
            throw NonExistentContinentException(continent)
        }
    }

    fun areBordering(firstCountry: String, secondCountry: String): Boolean {
        assertCountryExists(firstCountry)
        assertCountryExists(secondCountry)
        return borders.getValue(firstCountry).contains(secondCountry)
    }

    class Builder {

        private val continents = mutableMapOf<String, MutableCollection<String>>()
        private val borders = mutableMapOf<String, MutableCollection<String>>()

        fun addCountry(country: String, continent: String): Builder {
            assertCountryDoesNotExist(country)
            continents.getOrPut(continent) { mutableSetOf() }.add(country)
            borders[country] = mutableSetOf()
            return this
        }

        private fun assertCountryDoesNotExist(country: String) {
            if (doesCountryExist(country)) {
                throw CountryAlreadyExistsException(country)
            }
        }

        private fun doesCountryExist(country: String) =
            continents.values.any { it.contains(country) }

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
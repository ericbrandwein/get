class PoliticalMap private constructor(
    private val countriesWithContinents: Map<String, String>,
    private val borders: Map<String, Collection<String>>
) {

    val countries get() = countriesWithContinents.keys

    fun continentOf(country: String): String {
        assertCountryExists(country)
        return countriesWithContinents.getValue(country)
    }

    private fun assertCountryExists(country: String) {
        if (!countriesWithContinents.containsKey(country)) {
            throw NonExistentCountryException(country)
        }
    }

    fun countriesOf(continent: String): Set<String> {
        assertContinentExists(continent)
        return countriesWithContinents.filter { (_, eachContinent) ->
            eachContinent == continent
        }.keys
    }

    private fun assertContinentExists(continent: String) {
        if (!countriesWithContinents.containsValue(continent)) {
            throw NonExistentContinentException(continent)
        }
    }

    fun areBordering(firstCountry: String, secondCountry: String): Boolean {
        assertCountryExists(firstCountry)
        assertCountryExists(secondCountry)
        return borders.getValue(firstCountry).contains(secondCountry)
    }

    class Builder {

        private val countriesWithContinents = mutableMapOf<String, String>()
        private val borders = mutableMapOf<String, MutableCollection<String>>()

        fun addCountry(country: String, continent: String): Builder {
            assertCountryDoesNotExist(country)
            countriesWithContinents[country] = continent
            borders[country] = mutableSetOf()
            return this
        }

        private fun assertCountryDoesNotExist(country: String) {
            if (countriesWithContinents.containsKey(country)) {
                throw CountryAlreadyExistsException(country)
            }
        }

        fun addBorder(firstCountry: String, secondCountry: String): Builder {
            assertCountryExists(firstCountry)
            assertCountryExists(secondCountry)
            borders.getValue(firstCountry).add(secondCountry)
            borders.getValue(secondCountry).add(firstCountry)
            return this
        }

        private fun assertCountryExists(country: String) {
            if (!countriesWithContinents.containsKey(country)) {
                throw NonExistentCountryException(country)
            }
        }

        fun build() = PoliticalMap(countriesWithContinents, borders)
    }
}
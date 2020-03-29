typealias Country = String

class PoliticalMap private constructor(
    private val continentSet: ContinentSet,
    private val borders: Map<Country, Collection<Country>>
) {

    val countries = continentSet.countries
    val continents = continentSet

    fun continentOf(country: Country) = continentSet.forCountry(country)

    fun areBordering(firstCountry: Country, secondCountry: Country): Boolean {
        continentSet.assertCountryExists(firstCountry)
        continentSet.assertCountryExists(secondCountry)
        return borders.getValue(firstCountry).contains(secondCountry)
    }

    class Builder {

        private val continents = ContinentSet()
        private val borders = mutableMapOf<Country, MutableCollection<Country>>()
        private val countries = mutableSetOf<Country>()

        fun addContinent(continent: Continent): Builder {
            val intersection = countries.intersect(continent.countries)
            if (!intersection.isEmpty()) {
                throw CountryAlreadyExistsException(intersection.elementAt(0))
            }
            countries.addAll(continent.countries)
            continents.add(continent)
            continent.countries.forEach {borders[it] = mutableSetOf()}
            return this
        }

        fun addBorder(firstCountry: Country, secondCountry: Country): Builder {
            continents.assertCountryExists(firstCountry)
            continents.assertCountryExists(secondCountry)
            borders.getValue(firstCountry).add(secondCountry)
            borders.getValue(secondCountry).add(firstCountry)
            return this
        }

        fun build() = PoliticalMap(continents, borders)
    }
}

class ContinentSet : HashSet<Continent>() {

    val countries get() = flatMap { continent -> continent.countries }.toSet()

    fun forCountry(country: Country): Continent {
        try { return first { it.containsCountry(country) } }
        catch (e:  NoSuchElementException) {
            throw NonExistentCountryException(country)
        }

    }

    fun assertCountryExists(country: Country) {
        if (!anyHasCountry(country)) { throw NonExistentCountryException(country) }
    }

    fun anyHasCountry(country: Country) = any { it.containsCountry(country) }

    fun doesContinentExist(continent: Continent) = any { it == continent }
}

data class Continent(val name: String) {
    val countries = mutableSetOf<String>()

    fun addCountry(country: Country) : Continent {
        if (!countries.add(country)) { throw CountryAlreadyExistsException(country) }
        return this
    }

    fun containsCountry(country: Country) = countries.contains(country)
}

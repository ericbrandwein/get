typealias Country = String

data class Continent(val name: String, val countries: Set<Country>)

class PoliticalMap private constructor(
    public val continents: Set<Continent>,
    private val borders: Map<Country, Collection<Country>>
) {

    val countries = continents.countries

    fun continentOf(country: Country) = continents.forCountry(country)

    fun areBordering(firstCountry: Country, secondCountry: Country): Boolean {
        continents.assertCountryExists(firstCountry)
        continents.assertCountryExists(secondCountry)
        return borders.getValue(firstCountry).contains(secondCountry)
    }

    class Builder {

        private val continents = HashSet<Continent>()
        private val borders = mutableMapOf<Country, MutableCollection<Country>>()
        private val countries = HashSet<Country>()

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

val Set<Continent>.countries : Set<Country>
        get() = flatMap { continent -> continent.countries }.toSet()

fun Set<Continent>.forCountry(country: Country): Continent {
    try { return first { it.countries.contains(country) } }
    catch (e:  NoSuchElementException) {
        throw NonExistentCountryException(country)
    }

}

fun Set<Continent>.assertCountryExists(country: Country) {
    if (!any { it.countries.contains(country) } ) { throw NonExistentCountryException(country) }
}

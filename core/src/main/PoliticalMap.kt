typealias Country = String

class PoliticalMap private constructor(
    private val continents: ContinentSet,
    private val borders: Map<Country, Collection<Country>>
) {

    val countries get() = continents.countries

    fun continentOf(country: Country) = continents.forCountry(country)

    fun areBordering(firstCountry: Country, secondCountry: Country): Boolean {
        continents.assertCountryExists(firstCountry)
        continents.assertCountryExists(secondCountry)
        return borders.getValue(firstCountry).contains(secondCountry)
    }

    class Builder {

        private val continents = ContinentSet.new()
        private val borders = mutableMapOf<Country, MutableCollection<Country>>()

        fun addContinent(continent: Continent): Builder {
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

private class ContinentSet private constructor(
    val continents: MutableSet<Continent>
) : MutableSet<Continent> by continents {

    val countries get() = flatMap { continent -> continent.countries }.toSet()

    fun forCountry(country: Country): Continent {
        try { return first { it.containsCountry(country) } }
        catch (e:  NoSuchElementException) {
            throw NonExistentCountryException(country)
        }

    }

    fun assertCountryExists(country: Country) {
        if (!anyHasCountry(country)) {
            throw NonExistentCountryException(country)
        }
    }

    fun anyHasCountry(country: Country) = any { it.containsCountry(country) }

    fun doesContinentExist(continent: Continent) = any { it == continent }

    companion object {
        fun new() = ContinentSet(mutableSetOf())
    }
}

data class Continent(val name: String) {
    val countries = mutableSetOf<String>()

    fun addCountry(country: Country) : Continent {
        if (!countries.add(country)) {
            throw CountryAlreadyExistsException(country)
        }
        return this
    }

    fun containsCountry(country: Country) = countries.contains(country)
}
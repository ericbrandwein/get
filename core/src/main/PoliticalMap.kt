typealias Country = String

class PoliticalMap private constructor(
    private val continents: ContinentSet,
    private val borders: Map<Country, Collection<Country>>
) {

    val countries get() = continents.countries

    fun continentOf(country: Country) = continents.forCountry(country).name

    fun countriesOf(continent: String) =  continents.get(continent).countries

    fun areBordering(firstCountry: Country, secondCountry: Country): Boolean {
        continents.assertCountryExists(firstCountry)
        continents.assertCountryExists(secondCountry)
        return borders.getValue(firstCountry).contains(secondCountry)
    }

    class Builder {

        private val continents = ContinentSet.new()
        private val borders = mutableMapOf<Country, MutableCollection<Country>>()

        fun addCountry(country: Country, continent: String): Builder {
            continents.addCountry(country, continent)
            borders[country] = mutableSetOf()
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

    fun get(name: String): Continent {
        assertContinentExists(name)
        return getOrNull(name)!!
    }
    fun getOrPut(name: String) = getOrNull(name) ?: create(name)
    fun getOrNull(name: String) = firstOrNull { it.name == name }

    fun create(name: String): Continent {
        val continent = Continent(name)
        add(continent)
        return continent
    }

    fun addCountry(country: Country, continent: String) {
        assertCountryDoesNotExist(country)
        getOrPut(continent).addCountry(country)
    }

    fun forCountry(country: Country): Continent {
        assertCountryExists(country)
        return first { it.containsCountry(country) }
    }

    fun assertContinentExists(continent: String) {
        if (!doesContinentExist(continent)) {
            throw NonExistentContinentException(continent)
        }
    }

    fun assertCountryExists(country: Country) {
        if (!anyHasCountry(country)) {
            throw NonExistentCountryException(country)
        }
    }

    fun assertCountryDoesNotExist(country: Country) {
        if (anyHasCountry(country)) {
            throw CountryAlreadyExistsException(country)
        }
    }

    fun anyHasCountry(country: Country) = any { it.containsCountry(country) }

    fun doesContinentExist(name: String) = any { it.name == name }

    companion object {
        fun new() = ContinentSet(mutableSetOf())
    }
}

private class Continent(val name: String) {
    val countries = mutableSetOf<String>()

    fun addCountry(country: Country) = countries.add(country)
    fun containsCountry(country: Country) = countries.contains(country)
}
package countries

import Country
import Player
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class CountryDealerTest {

    private fun assertAllCountriesAreOccupied(
        countries: Set<Country>, occupations: Collection<Occupation>) {

        assertEquals(countries.size, occupations.size)
        val actualCountries = occupations.map { it.country }.toSet()
        assertEquals(countries, actualCountries)
    }

    private fun assertEachPlayerHasAnEqualPartOfTheCountries(
        players: Collection<Player>, occupations: Collection<Occupation>) {
        val playersAmount = players.size
        val countriesAmount = occupations.size
        val countriesPerPlayer = countriesAmount / playersAmount
        players.forEach { player ->
            val countriesOfCurrentPlayer = occupations.count { it.occupier == player }
            assertEquals(countriesPerPlayer, countriesOfCurrentPlayer)
        }
    }

    private fun assertAllOccupationsHaveOneArmy(occupations: Collection<Occupation>) {
        assertTrue(
            occupations.all { it.armies == 1 }, "All occupations should have one army")
    }

    @Test
    fun `Dealer with two players and two countries should give one to each player`() {
        val countries = setOf("Argentina", "Uruguay")
        val players = setOf("Juan", "Pedro")

        val occupations = CountryDealer(countries).dealTo(players)

        assertAllCountriesAreOccupied(countries, occupations)
        assertEachPlayerHasAnEqualPartOfTheCountries(players, occupations)
        assertAllOccupationsHaveOneArmy(occupations)
    }

    @Test
    fun `Dealer with two players and a bigger even amount of countries should give half to each player`() {
        val countries = setOf("Argentina", "Uruguay", "Peru", "Colombia")
        val players = setOf("Juan", "Pedro")

        val occupations = CountryDealer(countries).dealTo(players)

        assertAllCountriesAreOccupied(countries, occupations)
        assertEachPlayerHasAnEqualPartOfTheCountries(players, occupations)
        assertAllOccupationsHaveOneArmy(occupations)
    }

    @Test
    fun `Dealing an amount of countries divisible by the number of players should give the same amount to each player`() {
        val countries =
            setOf("Argentina", "Uruguay", "Peru", "Colombia", "Guyana", "Brasil")
        val players = setOf("Juan", "Pedro", "Domingo")

        val occupations = CountryDealer(countries).dealTo(players)

        assertAllCountriesAreOccupied(countries, occupations)
        assertEachPlayerHasAnEqualPartOfTheCountries(players, occupations)
        assertAllOccupationsHaveOneArmy(occupations)
    }

    @Test
    fun `Can't deal to zero players`() {
        val countries = setOf("Argentina", "Uruguay")
        val dealer = CountryDealer(countries)

        assertFailsWith<NoPlayersToDealToException> {
            dealer.dealTo(setOf())
        }
    }

    @Test
    fun `Dealing an odd amount of countries to two players gives the remaining country to one of them`() {
        val countries = setOf("Argentina", "Uruguay", "Peru")
        val players = setOf("Juan", "Pedro")

        val occupations = CountryDealer(countries).dealTo(players)

        assertAllCountriesAreOccupied(countries, occupations)
        assertAllOccupationsHaveOneArmy(occupations)
        players.forEach {player ->
            val occupationsForPlayer = occupations.count { it.occupier == player }
            assertTrue(occupationsForPlayer >= 1)
        }
    }

    @Test
    fun `Dealing an amount of countries not divisible by the amount of players gives one more to some of them`() {
        val countries = setOf("Argentina", "Uruguay", "Peru", "Brasil", "Guyana")
        val players = setOf("Juan", "Pedro", "Olivia")

        val occupations = CountryDealer(countries).dealTo(players)

        assertAllCountriesAreOccupied(countries, occupations)
        assertAllOccupationsHaveOneArmy(occupations)
        players.forEach {player ->
            val occupationsForPlayer = occupations.count { it.occupier == player }
            assertTrue(occupationsForPlayer in 1..2)
        }
    }

}

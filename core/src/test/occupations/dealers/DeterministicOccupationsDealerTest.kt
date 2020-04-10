package occupations.dealers

import Country
import Player
import occupations.Occupation
import PositiveInt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class DeterministicOccupationsDealerTest {
    private fun <T> assertSameElements(expected: Collection<T>, actual: Collection<T>) {
        assertEquals(expected.toSet(), actual.toSet())
    }

    private fun buildOccupationsFrom(
        countriesByPlayer: Map<Player, List<Country>>): List<Occupation> {
        return countriesByPlayer.flatMap { (player, countries) ->
            countries.map { country -> Occupation(country, player, PositiveInt(1)) }
        }
    }

    private fun assertDealerDealsCountriesToPlayersWithDistribution(
        players: List<Player>, countries: List<Country>,
        countriesDistribution: Map<Player, List<Country>>) {

        val actualOccupations = DeterministicOccupationsDealer(countries).dealTo(players)
        val expectedOccupations = buildOccupationsFrom(countriesDistribution)
        assertSameElements(expectedOccupations, actualOccupations)
    }

    @Test
    fun `Dealer with two players and two countries should give one to each player`() {
        val players = listOf("Juan", "Pedro")
        val countries = listOf("Argentina", "Uruguay")
        val countryDistribution = mapOf(
            players[0] to listOf(countries[0]),
            players[1] to listOf(countries[1])
        )

        assertDealerDealsCountriesToPlayersWithDistribution(
            players, countries, countryDistribution)
    }

    @Test
    fun `Dealer with two players and a bigger even amount of countries should give half to each player`() {
        val players = listOf("Juan", "Pedro")
        val countries = listOf("Argentina", "Uruguay", "Peru", "Colombia")
        val countriesGroupedByPlayer = countries.chunked(2)
        val countryDistribution = players.zip(countriesGroupedByPlayer).toMap()

        assertDealerDealsCountriesToPlayersWithDistribution(
            players, countries, countryDistribution)
    }

    @Test
    fun `Dealing an amount of countries divisible by the number of players should give the same amount to each player`() {
        val players = listOf("Juan", "Pedro", "Domingo")
        val countries =
            listOf("Argentina", "Uruguay", "Colombia", "Guyana", "Brasil", "Peru")
        val countriesGroupedByPlayer = countries.chunked(2)
        val countryDistribution = players.zip(countriesGroupedByPlayer).toMap()

        assertDealerDealsCountriesToPlayersWithDistribution(
            players, countries, countryDistribution)
    }

    @Test
    fun `Can't deal to zero players`() {
        val countries = listOf("Argentina", "Uruguay")
        val dealer = DeterministicOccupationsDealer(countries)

        assertFailsWith<NoPlayersToDealToException> {
            dealer.dealTo(setOf())
        }
    }

    @Test
    fun `Dealing an odd amount of countries to two players gives the remaining country to the first`() {
        val players = listOf("Juan", "Pedro")
        val countries = listOf("Argentina", "Uruguay", "Peru")
        val countryDistribution = mapOf(
            players[0] to listOf(countries[0], countries[2]),
            players[1] to listOf(countries[1])
        )

        assertDealerDealsCountriesToPlayersWithDistribution(
            players, countries, countryDistribution)
    }

    @Test
    fun `Dealing an amount of countries not divisible by the amount of players gives one more to the first ones`() {
        val players = listOf("Juan", "Pedro", "Olivia")
        val countries = listOf("Argentina", "Uruguay", "Peru", "Brasil", "Guyana")
        val countryDistribution = mapOf(
            players[0] to listOf(countries[0], countries[3]),
            players[1] to listOf(countries[1], countries[4]),
            players[2] to listOf(countries[2])
        )

        assertDealerDealsCountriesToPlayersWithDistribution(
            players, countries, countryDistribution)
    }
}

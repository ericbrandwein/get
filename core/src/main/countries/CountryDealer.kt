package countries

import Country
import Player

private const val STARTING_OCCUPATION_ARMIES = 1

class CountryDealer(private val countries: Collection<Country>) {

    fun dealTo(players: Collection<Player>): Collection<Occupation> {
        assertPlayersIsNotEmpty(players)
        val shuffledCountries = countries.shuffled()
        return dealEqually(shuffledCountries, players)
            .union(dealRemainingCountries(shuffledCountries, players))
    }

    private fun assertPlayersIsNotEmpty(players: Collection<Player>) {
        if (players.isEmpty()) {
            throw NoPlayersToDealToException()
        }
    }

    private fun dealEqually(
        countries: Collection<Country>, players: Collection<Player>
    ): Collection<Occupation> {
        val countriesPerPlayer = countries.size / players.size
        return deal(countries, players, countriesPerPlayer)
    }

    private fun dealRemainingCountries(
        countries: List<Country>, players: Collection<Player>
    ): Collection<Occupation> {
        val amountOfCountriesRemaining = countries.size % players.size
        val remainingCountries = countries.takeLast(amountOfCountriesRemaining)
        val shuffledPlayers = players.shuffled()
        return deal(remainingCountries, shuffledPlayers, 1)
    }

    private fun deal(
        countries: Collection<Country>, players: Collection<Player>,
        countriesForEachPlayer: Int
    ): Collection<Occupation> {
        val countriesWithPlayers = countries.chunked(countriesForEachPlayer).zip(players)
        return countriesWithPlayers
            .flatMap { (countries, player) -> occupationsForCountries(countries, player) }
    }

    private fun occupationsForCountries(countries: List<Country>, player: Player) =
        countries.map { country -> getStartingOccupation(country, player) }

    private fun getStartingOccupation(country: Country, player: Player) =
        Occupation(country, player, STARTING_OCCUPATION_ARMIES)
}

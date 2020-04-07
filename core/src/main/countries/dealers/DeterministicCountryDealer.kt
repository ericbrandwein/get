package countries.dealers

import Country
import Player
import countries.Occupation

class DeterministicCountryDealer(countries: List<Country>) : CountryDealer(countries) {

    override fun dealTo(players: Collection<Player>): Collection<Occupation> {
        assertPlayersIsNotEmpty(players)
        return dealEqually(players).union(dealRemaining(players))
    }

    private fun assertPlayersIsNotEmpty(players: Collection<Player>) {
        if (players.isEmpty()) {
            throw NoPlayersToDealToException()
        }
    }

    private fun dealEqually(players: Collection<Player>): Collection<Occupation> {
        val countriesPerPlayer = countries.size / players.size
        return deal(countries, players, countriesPerPlayer)
    }

    private fun dealRemaining(players: Collection<Player>): Collection<Occupation> {
        val amountOfCountriesRemaining = countries.size % players.size
        val remainingCountries = countries.takeLast(amountOfCountriesRemaining)
        return deal(remainingCountries, players, 1)
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

package occupations.dealers

import Country
import Player

class RandomCountryDealer(countries: List<Country>) : CountryDealer(countries) {
    override fun dealTo(players: Collection<Player>) =
        DeterministicCountryDealer(countries.shuffled()).dealTo(players.shuffled())
}

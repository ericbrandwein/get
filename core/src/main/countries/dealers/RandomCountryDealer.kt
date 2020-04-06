package countries.dealers

import Country
import Player

class RandomCountryDealer(private val countries: List<Country>) : CountryDealer {
    override fun dealTo(players: Collection<Player>) =
        DeterministicCountryDealer(countries.shuffled()).dealTo(players.shuffled())
}

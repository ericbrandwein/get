package countries.occupations.dealers

import countries.Country
import Player
import countries.occupations.Occupation

abstract class CountryDealer(protected val countries: List<Country>) {

    abstract fun dealTo(players: Collection<Player>): Collection<Occupation>

    companion object {
        const val STARTING_OCCUPATION_ARMIES = 1
    }
}

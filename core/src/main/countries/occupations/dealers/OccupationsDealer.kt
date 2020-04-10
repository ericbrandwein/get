package countries.occupations.dealers

import Country
import Player
import PositiveInt
import countries.occupations.Occupation

abstract class OccupationsDealer(protected val countries: List<Country>) {

    abstract fun dealTo(players: Collection<Player>): Collection<Occupation>

    companion object {
        val STARTING_OCCUPATION_ARMIES = PositiveInt(1)
    }
}

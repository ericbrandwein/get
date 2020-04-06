package countries.dealers

import Player
import countries.Occupation

interface CountryDealer {
    fun dealTo(players: Collection<Player>): Collection<Occupation>
}

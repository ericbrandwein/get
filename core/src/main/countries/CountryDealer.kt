package countries

import Player

interface CountryDealer {
    fun dealTo(players: Collection<Player>): Collection<Occupation>
}

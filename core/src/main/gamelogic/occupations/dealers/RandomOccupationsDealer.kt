package gamelogic.occupations.dealers

import Country
import Player

class RandomOccupationsDealer(countries: List<Country>) : OccupationsDealer(countries) {
    override fun dealTo(players: Collection<Player>) =
        DeterministicOccupationsDealer(countries.shuffled()).dealTo(players.shuffled())
}

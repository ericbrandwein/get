package gamelogic.occupations.dealers

import Player
import gamelogic.occupations.Occupation

class FixedOccupationsDealer(
    private val occupations: Collection<Occupation>,
    private val players: Collection<Player>
) : OccupationsDealer(occupations.map { it.country }) {
    override fun dealTo(players: Collection<Player>): Collection<Occupation> =
        if (players.toSet() == this.players.toSet()) occupations else emptyList()

}

package gamelogic

import gamelogic.map.PoliticalMap
import gamelogic.occupations.CountryOccupations

class Referee (val players:List<Player>, val politicalMap: PoliticalMap, val occupations: CountryOccupations){
    var player_index = 0

    fun nextPlayer() {
        players[(player_index + 1) % players.size]
    }

    private fun changeTurn(){
        ++player_index
    }
}
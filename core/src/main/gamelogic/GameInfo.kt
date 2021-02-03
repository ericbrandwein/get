package gamelogic

import gamelogic.map.PoliticalMap
import gamelogic.occupations.CountryOccupations

data class GameInfo(
    val players: List<PlayerInfo>,
    val playerIterator: LoopingIterator<PlayerInfo>,
    val politicalMap: PoliticalMap,
    val occupations: CountryOccupations,
    val destroyedPlayers: PlayerDestructions
)

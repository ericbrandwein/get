package gamelogic

import gamelogic.combat.AttackerFactory
import gamelogic.map.PoliticalMap
import gamelogic.occupations.CountryOccupations

data class GameInfo(
    var state: GameState,
    val attackerFactory: AttackerFactory,
    val players: List<PlayerInfo>,
    val playerIterator: LoopingIterator<PlayerInfo>,
    val politicalMap: PoliticalMap,
    val occupations: CountryOccupations,
    val destroyedPlayers: PlayerDestructions
) {
    val currentPlayer
        get() = playerIterator.current

    fun nextTurn() = playerIterator.next()
}

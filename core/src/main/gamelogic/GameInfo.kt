package gamelogic

import gamelogic.combat.AttackerFactory
import gamelogic.gameState.GameState
import gamelogic.map.PoliticalMap
import gamelogic.occupations.CountryOccupations

data class GameInfo(
    var state: GameState,
    val attackerFactory: AttackerFactory,
    val players: MutableList<PlayerInfo>,
    val politicalMap: PoliticalMap,
    val occupations: CountryOccupations,
    val destroyedPlayers: PlayerDestructions
) {

    val playerIterator = players.loopingIterator()

    val currentPlayer
        get() = playerIterator.current

    fun nextTurn() = playerIterator.next()
}

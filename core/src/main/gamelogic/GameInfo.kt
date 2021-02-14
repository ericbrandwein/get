package gamelogic

import gamelogic.combat.AttackerFactory
import gamelogic.gameState.GameState
import gamelogic.map.PoliticalMap
import gamelogic.occupations.CountryOccupations
import gamelogic.occupations.dealers.OccupationsDealer

class GameInfo(
    var state: GameState,
    val attackerFactory: AttackerFactory,
    val players: MutableList<PlayerInfo>,
    val politicalMap: PoliticalMap,
    val destroyedPlayers: PlayerDestructions,
    occupationsDealer: OccupationsDealer
) {

    val playerIterator = players.loopingIterator()
    val occupations = CountryOccupations(occupationsDealer.dealTo(players.map { it.name }))

    val currentPlayer
        get() = playerIterator.current

    fun nextTurn() = playerIterator.next()
}

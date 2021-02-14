package gamelogic

import gamelogic.combat.AttackerFactory
import gamelogic.gameState.GameState
import gamelogic.gameState.ReinforceState
import gamelogic.map.PoliticalMap
import gamelogic.occupations.CountryOccupations
import gamelogic.occupations.dealers.OccupationsDealer

class GameInfo(
    val players: MutableList<PlayerInfo>,
    val politicalMap: PoliticalMap,
    occupationsDealer: OccupationsDealer,
    val attackerFactory: AttackerFactory,
    val destroyedPlayers: PlayerDestructions = PlayerDestructions()
) {

    var state: GameState = ReinforceState(this)
    val playerIterator = players.loopingIterator()
    val occupations = CountryOccupations(occupationsDealer.dealTo(players.map { it.name }))

    val currentPlayer
        get() = playerIterator.current

    fun nextTurn() = playerIterator.next()
}

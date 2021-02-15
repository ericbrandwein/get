package gamelogic.gameState

import gamelogic.CountryReinforcement
import gamelogic.GameInfo

class ReinforceState(private val gameInfo: GameInfo) : GameState() {
    override fun addArmies(reinforcements: Collection<CountryReinforcement>) {
        reinforcements.forEach {
            it.apply(gameInfo.playerIterator.current.name, gameInfo.occupations)
        }
        gameInfo.state = AttackState(gameInfo)
    }
}

package gamelogic.gameState

import gamelogic.GameInfo
import gamelogic.Regrouping

class RegroupState(private val gameInfo: GameInfo) : GameState() {
    override fun regroup(regroupings: List<Regrouping>) {
        validateRegroupings(regroupings)
        regroupings.map { it.apply(gameInfo.occupations) }
        gameInfo.nextTurn()
        gameInfo.state = ReinforceState(gameInfo)
    }

    private fun validateRegroupings(regroupings: List<Regrouping>) {
        regroupings.forEach { it.validate(gameInfo) }
        val occupations = gameInfo.occupations
        val playerName = gameInfo.currentPlayer.name
        if (
            regroupings.any {
                occupations.occupierOf(it.from) != playerName ||
                    occupations.occupierOf(it.to) != playerName
            }
        ) {
            throw Exception("player must occupy both countries to regroup")
        }

        if (regroupings.distinctBy { it.from }.count() != regroupings.count()) {
            throw Exception(
                "Only one regroup per country per turn is allowed (to facilitate validation)")
        }
    }
}

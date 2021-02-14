package gamelogic.gameState

import Country
import gamelogic.CountryIsNotOccupiedByPlayerException
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
        regroupings.forEach {
            it.validate(gameInfo)
            assertPlayerOccupiesCountry(it.from)
            assertPlayerOccupiesCountry(it.to)
        }

        if (regroupings.distinctBy { it.from }.count() != regroupings.count()) {
            throw Exception(
                "Only one regroup per country per turn is allowed (to facilitate validation)")
        }
    }

    private fun assertPlayerOccupiesCountry(country: Country) {
        CountryIsNotOccupiedByPlayerException(country, gameInfo.currentPlayer.name)
            .assertPlayerOccupiesCountryIn(gameInfo.occupations)
    }
}

package gamelogic.gameState

import Country
import Player
import gamelogic.GameInfo
import gamelogic.Regrouping
import gamelogic.occupations.CountryOccupations

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

class CountryIsNotOccupiedByPlayerException(val country: Country, val player: Player) :
    Exception("Country $country is not occupied by $player.")
{
    fun assertPlayerOccupiesCountryIn(occupations: CountryOccupations) {
        if (occupations.occupierOf(country) != player) {
            throw this
        }
    }
}

package gamelogic.gameState

import Country
import PositiveInt
import gamelogic.CountryReinforcement
import gamelogic.Regrouping

abstract class GameState {
    open fun addArmies(reinforcements: Collection<CountryReinforcement>): Unit =
        throw NotInReinforcingStateException()

    open fun makeAttack(from: Country, to: Country): Unit =
        throw Exception("Cannot attack when not in attacking state")

    open fun occupyConqueredCountry(armies: PositiveInt): Unit =
        throw Exception("Cannot occupy when not in attacking state")

    open fun endAttack(): Unit = throw Exception("Cannot end attack if not attacking")

    open fun regroup(regroupings: List<Regrouping>): Unit =
        throw Exception("Cannot regroup if not regrouping")
}

object NoState : GameState()

class NotInReinforcingStateException : Exception("Cannot add armies right now.")

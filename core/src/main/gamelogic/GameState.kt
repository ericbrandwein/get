package gamelogic

import Country
import PositiveInt
import gamelogic.combat.Occupier
import gamelogic.situations.classicCombat.ClassicCombatDiceAmountCalculator

sealed class GameState {
    open fun addArmies(reinforcements: Collection<CountryReinforcement>): Unit =
        throw NotInReinforcingStageException()

    open fun makeAttack(from: Country, to: Country): Unit =
        throw Exception("Cannot attack when not in attacking state")

    open fun occupyConqueredCountry(armies: PositiveInt): Unit =
        throw Exception("Cannot occupy when not in attacking state")

    open fun endAttack(): Unit = throw Exception("Cannot end attack if not attacking")

    open fun regroup(regroupings: List<Regrouping>): Unit =
        throw Exception("Cannot regroup if not regrouping")
}

object NoState : GameState()

class ReinforceState(private val gameInfo: GameInfo) : GameState() {
    override fun addArmies(reinforcements: Collection<CountryReinforcement>) {
        reinforcements.forEach {
            it.apply(gameInfo.playerIterator.current.name, gameInfo.occupations)
        }
        gameInfo.state = AttackState(gameInfo)
    }
}

class AttackState(private val gameInfo: GameInfo) : GameState() {
    private var state: State = FightState()

    override fun makeAttack(from: Country, to: Country) = state.makeAttack(from, to)

    override fun occupyConqueredCountry(armies: PositiveInt) =
        state.occupyConqueredCountry(armies)

    override fun endAttack() = state.endAttack()

    private interface State {
        fun makeAttack(from: Country, to: Country)
        fun occupyConqueredCountry(armies: PositiveInt)
        fun endAttack()
    }

    private inner class FightState : State {
        override fun makeAttack(from: Country, to: Country) {
            val occupations = gameInfo.occupations
            val currentPlayerName = gameInfo.currentPlayer.name
            if (occupations.occupierOf(from) != currentPlayerName) {
                throw Exception("Player $currentPlayerName does not occupy $from")
            }
            val politicalMap = gameInfo.politicalMap
            CountriesAreNotBorderingException(from, to).assertAreBorderingIn(politicalMap)
            val attacker = gameInfo.attackerFactory.create(
                occupations, ClassicCombatDiceAmountCalculator()
            )
            attacker.attack(from, to)
            if (occupations.isEmpty(to)) {
                state = OccupyingState(from, to)
            }
        }

        override fun occupyConqueredCountry(armies: PositiveInt) =
            throw Exception("Not the moment to occupy conquered country")

        override fun endAttack() {
            gameInfo.state = RegroupState(gameInfo)
        }
    }

    private inner class OccupyingState(
        private val from: Country, private val to: Country
    ) : State {
        private val occupier = Occupier(gameInfo.occupations)

        override fun makeAttack(from: Country, to: Country) =
            throw Exception("Cannot attack if not fighting")

        override fun occupyConqueredCountry(armies: PositiveInt) {
            occupier.occupy(from, to, armies)
            state = FightState()
        }

        override fun endAttack() = throw CannotEndAttackWhenOccupyingException()
    }
}

class CannotEndAttackWhenOccupyingException : Exception(
    "Cannot end attack when there is a country that must be occupied."
)

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

class NotInReinforcingStageException : Exception("Cannot add armies right now.")

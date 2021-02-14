package gamelogic.gameState

import Country
import PositiveInt
import gamelogic.CountriesAreNotBorderingException
import gamelogic.GameInfo
import gamelogic.combat.Occupier
import gamelogic.situations.classicCombat.ClassicCombatDiceAmountCalculator

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
            CountryIsNotOccupiedByPlayerException(from, gameInfo.currentPlayer.name)
                .assertPlayerOccupiesCountryIn(occupations)
            CountriesAreNotBorderingException(from, to)
                .assertAreBorderingIn(gameInfo.politicalMap)
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

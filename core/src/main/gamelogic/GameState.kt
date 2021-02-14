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
    enum class AttackState { Fight, Occupation }
    private var attackState = AttackState.Fight
    private val occupier = Occupier(gameInfo.occupations)
    private var occupyingFrom: Country? = null
    private var occupyingTo: Country? = null


    override fun makeAttack(from: Country, to: Country) {
        if (attackState != AttackState.Fight) {
            throw Exception("Cannot attack if not fighting")
        }
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
            attackState = AttackState.Occupation
            occupyingFrom = from
            occupyingTo = to
        }
    }

    override fun occupyConqueredCountry(armies: PositiveInt) {
        if (attackState != AttackState.Occupation) {
            throw Exception("Not the moment to occupy conquered country")
        }

        occupier.occupy(occupyingFrom!!, occupyingTo!!, armies)
        attackState = AttackState.Fight
    }

    override fun endAttack() {
        gameInfo.state = RegroupState(gameInfo)
    }
}

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

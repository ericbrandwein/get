package gamelogic

import Country
import Player
import PositiveInt
import gamelogic.combat.Attack
import gamelogic.combat.Attacker
import gamelogic.combat.Conqueror
import gamelogic.map.PoliticalMap
import gamelogic.occupations.CountryOccupations
import gamelogic.situations.classicCombat.ClassicCombatDiceAmountCalculator


//TODO: En lugar de crear esta clase lo que hay que hacer es sacar del ataquer la
//      responsabilidad de mover las fichas después de atacar. Peeero, no solo hay
//      que hacer refactor del package combat, sino también de los tests!!! Asi que
//      queda para otro backlog item.
class SkipRegroup : Conqueror {
    override fun armiesToMove(remainingAttackingArmies: PositiveInt): PositiveInt {
        return PositiveInt(1)
    }

}

class CountryReinforcement(val country:Country, val armies: PositiveInt) {
    fun apply(player: Player, occupations: CountryOccupations) {
        if (occupations.occupierOf(country) != player) {
            throw Exception("Player ${player} cannot add army to country")
        }
        occupations.addArmies(country, armies)
    }
}

/**
 * preconditions (checked in [Referee.validateRegroupings] method):
 *      1. `regroupings.distinctBy { it.from }.count() == regroupings.count()`
 *      2. occupier of from and to is the same as the currentPlayer
 */
class Regrouping(val from: Country, val to: Country, val armies: PositiveInt) {
    fun validate(gameInfo: GameInfo) {
        if (gameInfo.occupations.armiesOf(from) <= armies) {
            throw Exception(
                "Cannot move ${armies.toInt()} armies if they are not available in country")
        }
        if (!gameInfo.politicalMap.areBordering(from, to)) {
            throw Exception(
                "countries must be bordering to regroup but $from and $to are not")
        }
    }

    fun apply(occupations: CountryOccupations) {
        occupations.removeArmies(from, armies)
        occupations.addArmies(to, armies)
    }
}
/**
 * The Referee of the game
 *
 * @property players the players sorted according to their turns
 * @property politicalMap the board with countries, continents and connexions between countries
 * @property occupations the players' occupations (Starts being the initial setting, it is updated after each attack).
 *
 * Referee has 3 states.
 * AddArmies: when current player adds armies to his countries (this is one action)
 * Attack: when current player attacks any enemy. This action may be repeated
 * Regroup: when current player moves his armies after the Attack phase
 */
class Referee(
    private val players: MutableList<PlayerInfo>,
    private val politicalMap: PoliticalMap,
    val occupations: CountryOccupations
) {
    enum class State {
        AddArmies {
            override fun next(referee: Referee): State = Attack
        },
        Attack {
            override fun next(referee: Referee): State = Regroup
        },
        Regroup {
            override fun next(referee: Referee): State {
                referee.changeTurn();
                return AddArmies
            }
        };

        abstract fun next(referee: Referee): State
    }

    enum class AttackState { Fight, Occupation }

    private var playerIterator = players.loopingIterator()
    private var state = State.AddArmies
    private var attackState = AttackState.Fight
    private var currentAttack: Attack? = null

    private val gameInfo
        get() = GameInfo(
            players, playerIterator, politicalMap, occupations, PlayerDestructions())

    val currentState: State
        get() = state

    val currentAttackState: AttackState
        get() = attackState

    val currentPlayer
        get() = playerIterator.current.name

    val gameIsOver : Boolean
        get() = players.any { it.reachedTheGoal(gameInfo) }

    val winners : List<Player>
        get() = players.filter { it.reachedTheGoal(gameInfo) }.map { it.name }

    private fun toNextState() {
        state = state.next(this)
    }

    private fun changeTurn() = playerIterator.next()

    fun addArmies(reinforcements: List<CountryReinforcement>) {
        reinforcements.forEach { it.apply(currentPlayer, occupations) }
        toNextState()
    }

    fun makeAttack(from:Country, to:Country) {
        if (state != State.Attack) {
            throw Exception("Cannot attack when not in attacking state")
        } else if (attackState != AttackState.Fight) {
            throw Exception("Cannot attack if not fighting")
        }
        if (occupations.occupierOf(from) != currentPlayer) {
            throw Exception("Player $currentPlayer does not occupy $from")
        }

        val attacker = Attacker.withDiceAmountCalculator(
            occupations, ClassicCombatDiceAmountCalculator()
        )
        val attack = attacker.makeAttack(from, to)
        if (attack.isConquering) {
            attackState = AttackState.Occupation
            currentAttack = attack
        } else {
            attack.apply()
        }
    }

    fun occupyConqueredCountry(armies: PositiveInt) {
        if (state != State.Attack || attackState != AttackState.Occupation) {
           throw Exception("Not the moment to occupy conquered country")
        }
        currentAttack!!.apply(armies)
        attackState = AttackState.Fight
        currentAttack = null
    }

    fun endAttack() {
        if (state != State.Attack) {
            throw Exception("Cannot end attack if not attacking")
        }
        toNextState()
    }

    fun validateRegroupings(regroupings: List<Regrouping>) {
        regroupings.forEach { it.validate(gameInfo) }
        if(regroupings.any{ occupations.occupierOf(it.from) != currentPlayer || occupations.occupierOf(it.to) != currentPlayer }) {
            throw Exception("player must occupy both countries to regroup")
        }

        if (regroupings.distinctBy { it.from }.count() != regroupings.count()) {
            throw Exception("Only one regroup per country per turn is allowed (to facilitate validation)")
        }
    }

    fun regroup(regroupings: List<Regrouping>){
        if (state != State.Regroup) { throw Exception("Cannot regroup if not regrouping") }
        validateRegroupings(regroupings)
        regroupings.map { it.apply(occupations) }
        toNextState()
    }
}

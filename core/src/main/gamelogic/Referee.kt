package gamelogic

import Country
import Player
import PositiveInt
import gamelogic.combat.AttackerFactory
import gamelogic.combat.DiceRollingAttackerFactory
import gamelogic.combat.Occupier
import gamelogic.map.PoliticalMap
import gamelogic.occupations.CountryOccupations
import gamelogic.occupations.dealers.OccupationsDealer
import gamelogic.occupations.dealers.RandomOccupationsDealer
import gamelogic.situations.classicCombat.ClassicCombatDiceAmountCalculator

class CountryReinforcement(val country:Country, val armies: PositiveInt) {
    fun apply(player: Player, occupations: CountryOccupations) {
        if (occupations.occupierOf(country) != player) {
            throw Exception("Player $player cannot add army to country")
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
        if (gameInfo.occupations.armiesOf(from) <= armies.toInt()) {
            throw Exception(
                "Cannot move ${armies.toInt()} armies if they are not available in country")
        }
        CountriesAreNotBorderingException(from, to)
            .assertAreBorderingIn(gameInfo.politicalMap)
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
    occupationsDealer: OccupationsDealer =
        RandomOccupationsDealer(politicalMap.countries.toList()),
    private val attackerFactory: AttackerFactory = DiceRollingAttackerFactory()
) {
    enum class AttackState { Fight, Occupation }

    val occupations =
        CountryOccupations(occupationsDealer.dealTo(players.map { it.name }))
    private var playerIterator = players.loopingIterator()
    private var state = GameState.AddArmies
    private var attackState = AttackState.Fight
    private val occupier = Occupier(occupations)
    private var occupyingFrom: Country? = null
    private var occupyingTo: Country? = null


    private val gameInfo
        get() = GameInfo(
            players, playerIterator, politicalMap, occupations, PlayerDestructions())

    val currentState: GameState
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
        state = state.next(gameInfo)
    }

    fun addArmies(reinforcements: List<CountryReinforcement>) {
        if (state != GameState.AddArmies) {
            throw NotInReinforcingStageException()
        }
        reinforcements.forEach { it.apply(currentPlayer, occupations) }
        toNextState()
    }

    fun makeAttack(from:Country, to:Country) {
        if (state != GameState.Attack) {

            throw Exception("Cannot attack when not in attacking state")
        } else if (attackState != AttackState.Fight) {
            throw Exception("Cannot attack if not fighting")
        }
        if (occupations.occupierOf(from) != currentPlayer) {
            throw Exception("Player $currentPlayer does not occupy $from")
        }
        CountriesAreNotBorderingException(from, to).assertAreBorderingIn(politicalMap)
        val attacker = attackerFactory.create(
            occupations, ClassicCombatDiceAmountCalculator()
        )
        attacker.attack(from, to)
        if (occupations.isEmpty(to)) {
            attackState = AttackState.Occupation
            occupyingFrom = from
            occupyingTo = to
        }
    }

    fun occupyConqueredCountry(armies: PositiveInt) {
        if (state != GameState.Attack || attackState != AttackState.Occupation) {
           throw Exception("Not the moment to occupy conquered country")
        }

        occupier.occupy(occupyingFrom!!, occupyingTo!!, armies)
        attackState = AttackState.Fight
    }

    fun endAttack() {
        if (state != GameState.Attack) {
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
        if (state != GameState.Regroup) { throw Exception("Cannot regroup if not regrouping") }
        validateRegroupings(regroupings)
        regroupings.map { it.apply(occupations) }
        toNextState()
    }
}

class CountriesAreNotBorderingException(val from: Country, val to: Country) :
    Exception("Countries $from and $to are not bordering.")
{
    fun assertAreBorderingIn(politicalMap: PoliticalMap) {
        if (!politicalMap.areBordering(from, to)) {
            throw this
        }
    }
}

class NotInReinforcingStageException : Exception("Cannot add armies right now.")

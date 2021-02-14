package gamelogic

import Country
import Player
import PositiveInt
import gamelogic.combat.AttackerFactory
import gamelogic.combat.DiceRollingAttackerFactory
import gamelogic.gameState.GameState
import gamelogic.gameState.NoState
import gamelogic.gameState.ReinforceState
import gamelogic.map.PoliticalMap
import gamelogic.occupations.CountryOccupations
import gamelogic.occupations.dealers.OccupationsDealer
import gamelogic.occupations.dealers.RandomOccupationsDealer

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
    attackerFactory: AttackerFactory = DiceRollingAttackerFactory()
) {

    val occupations =
        CountryOccupations(occupationsDealer.dealTo(players.map { it.name }))

    private val gameInfo = GameInfo(
        NoState, attackerFactory,
        players, politicalMap, occupations,
        PlayerDestructions()
    )

    val currentPlayer
        get() = gameInfo.currentPlayer.name

    private var state: GameState
        get() = gameInfo.state
        set(value) {
            gameInfo.state = value
        }

    val gameIsOver: Boolean
        get() = players.any { it.reachedTheGoal(gameInfo) }

    val winners: List<Player>
        get() = players.filter { it.reachedTheGoal(gameInfo) }.map { it.name }

    init {
        state = ReinforceState(gameInfo)
    }

    fun addArmies(reinforcements: List<CountryReinforcement>) =
        state.addArmies(reinforcements)

    fun makeAttack(from: Country, to: Country) = state.makeAttack(from, to)

    fun occupyConqueredCountry(armies: PositiveInt) = state.occupyConqueredCountry(armies)

    fun endAttack() = state.endAttack()

    fun regroup(regroupings: List<Regrouping>) = state.regroup(regroupings)
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


package gamelogic

import Country
import Player
import PositiveInt
import gamelogic.combat.AttackerFactory
import gamelogic.combat.DiceRollingAttackerFactory
import gamelogic.gameState.GameState
import gamelogic.map.PoliticalMap
import gamelogic.occupations.CountryOccupations
import gamelogic.occupations.dealers.OccupationsDealer
import gamelogic.occupations.dealers.RandomOccupationsDealer

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
    val occupations: CountryOccupations,
    attackerFactory: AttackerFactory
) {

    private val gameInfo = GameInfo(
        players, politicalMap, occupations, attackerFactory
    )

    val currentPlayer
        get() = gameInfo.currentPlayer.name

    private val state: GameState
        get() = gameInfo.state

    val gameIsOver: Boolean
        get() = players.any { it.reachedTheGoal(gameInfo) }

    val winners: List<Player>
        get() = players.filter { it.reachedTheGoal(gameInfo) }.map { it.name }

    fun addArmies(reinforcements: List<CountryReinforcement>) =
        state.addArmies(reinforcements)

    fun makeAttack(from: Country, to: Country) = state.makeAttack(from, to)

    fun occupyConqueredCountry(armies: PositiveInt) = state.occupyConqueredCountry(armies)

    fun endAttack() = state.endAttack()

    fun regroup(regroupings: List<Regrouping>) = state.regroup(regroupings)

    companion object {
        fun initialize(
            players: MutableList<PlayerInfo>, politicalMap: PoliticalMap,
            occupationsDealer: OccupationsDealer =
                RandomOccupationsDealer(politicalMap.countries.toList()),
            attackerFactory: AttackerFactory = DiceRollingAttackerFactory()
        ): Referee {
            val occupations = CountryOccupations(
                occupationsDealer.dealTo(players.map { it.name })
            )
            return Referee(players, politicalMap, occupations, attackerFactory)
        }
    }
}

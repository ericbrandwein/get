package gamelogic

import Country
import Player
import PositiveInt
import gamelogic.combat.AttackerFactory
import gamelogic.combat.DiceRollingAttackerFactory
import gamelogic.gameState.GameState
import gamelogic.map.PoliticalMap
import gamelogic.occupations.CountryOccupations
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
    attackerFactory: AttackerFactory = DiceRollingAttackerFactory()
) {

    init {
        PlayersSharingColorException.assertNoPlayersShareColor(players)
    }

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

    fun goalOf(player: Player) = players.first { it.name == player }.goal

    fun addArmies(reinforcements: List<CountryReinforcement>) =
        state.addArmies(reinforcements)

    fun makeAttack(from: Country, to: Country) = state.makeAttack(from, to)

    fun occupyConqueredCountry(armies: PositiveInt) = state.occupyConqueredCountry(armies)

    fun endAttack() = state.endAttack()

    fun regroup(regroupings: List<Regrouping>) = state.regroup(regroupings)

    companion object {
        fun forGame(
            playerColors: Map<Player, Color>,
            politicalMap: PoliticalMap,
            goals: Collection<Goal>
        ): Referee {
            val playerNames = playerColors.keys
            val occupations = RandomOccupationsDealer(politicalMap.countries.toList())
                .dealTo(playerNames)
            val playerInfos = dealGoals(playerColors, goals)
            return Referee(
                playerInfos, politicalMap,
                CountryOccupations(occupations),
                DiceRollingAttackerFactory()
            )
        }

        private fun dealGoals(
            playerColors: Map<Player, Color>, goals: Collection<Goal>
        ): MutableList<PlayerInfo> {
            NotEnoughGoalsToDealException(goals.size, playerColors.size)
                .assertEnoughGoalsForPlayers()
            return playerColors.entries
                .zip(goals.shuffled())
                .map { (entry, goal) -> PlayerInfo(entry.key, entry.value, goal) }
                .toMutableList()
        }
    }
}

class PlayersSharingColorException(
    val players: List<Player>, val color: Color
) : IllegalArgumentException(
    "Players ${players[0]} and $players[1]} have the same color $color."
) {
    companion object {
        fun assertNoPlayersShareColor(players: List<PlayerInfo>) {
            players.groupBy { it.color }.entries
                .firstOrNull { (_, players) -> players.size > 1 }
                ?.let { (color, players) ->
                    throw PlayersSharingColorException(players.map { it.name }, color)
                }
        }
    }
}

class NotEnoughGoalsToDealException(
    val goalsAmount: Int, val playersAmount: Int
) : IllegalArgumentException(
    "Cannot deal enough goals; there are $goalsAmount goals, but $playersAmount players."
) {
    fun assertEnoughGoalsForPlayers() {
        if (goalsAmount < playersAmount) {
            throw this
        }
    }
}

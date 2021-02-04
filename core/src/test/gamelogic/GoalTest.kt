package gamelogic

import Player
import PositiveInt
import gamelogic.map.Continent
import gamelogic.map.PoliticalMap
import gamelogic.occupations.CountryOccupations
import gamelogic.occupations.Occupation
import org.junit.Test
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertTrue

private const val A_COUNTRY = "Argentina"
private val SINGLE_COUNTRY_CONTINENT = Continent("América", setOf(A_COUNTRY))
private val SINGLE_COUNTRY_POLITICAL_MAP =
    PoliticalMap.Builder().addContinent(SINGLE_COUNTRY_CONTINENT).build()
private val SINGLE_COUNTRY_OCCUPATIONS =
    CountryOccupations(listOf(Occupation(A_COUNTRY, "Nico", PositiveInt(1))))

private fun twoPlayersWithGoals(firstGoal: Goal, secondGoal: Goal) =
    mutableListOf(
        PlayerInfo("Eric", Color.White, firstGoal),
        PlayerInfo("Nico", Color.White, secondGoal)
    )

private fun createSingleCountryGameInfo(
    players: MutableList<PlayerInfo>, destroyed: PlayerDestructions = PlayerDestructions()
) =
    createGameInfo(
        players, SINGLE_COUNTRY_POLITICAL_MAP, SINGLE_COUNTRY_OCCUPATIONS, destroyed)

private fun createGameInfo(
    players: MutableList<PlayerInfo>,
    politicalMap: PoliticalMap,
    occupations: CountryOccupations,
    destroyed: PlayerDestructions = PlayerDestructions()
) = GameInfo(players, players.loopingIterator(), politicalMap, occupations, destroyed)

class OccupyContinentTest {
    private val goal: Goal = Goal(listOf(OccupyContinent(SINGLE_COUNTRY_CONTINENT)))
    private val notOccupierInfo: PlayerInfo
    private val occupierInfo: PlayerInfo
    private val gameInfo: GameInfo

    init {
        val players = twoPlayersWithGoals(goal, goal)
        notOccupierInfo = players[0]
        occupierInfo = players[1]
        gameInfo = createSingleCountryGameInfo(players)
    }

    @Test
    fun `Is not achieved when there's at least one country not occupied by the player`() {
        assertFalse(goal.achieved(notOccupierInfo, gameInfo))
    }

    @Test
    fun `Is achieved when all countries are occupied by the player`() {
        assertTrue(goal.achieved(occupierInfo, gameInfo))
    }
}

class OccupySubContinentTest {
    @Test
    fun `Can't create goal with more countries to occupy than the amount there are in the Continent`() {
        assertFails {
            singleCountryContinentGoalWithCountries(2)
        }
    }

    @Test
    fun `Is not achieved when there are still countries to be occupied`() {
        val goal = singleCountryContinentGoalWithCountries(1)
        val players = twoPlayersWithGoals(goal, goal)
        val gameInfo = createSingleCountryGameInfo(players)
        val notOccupierInfo = players[0]

        assertFalse(goal.achieved(notOccupierInfo, gameInfo))
    }

    @Test
    fun `Is achieved when all countries in the Continent are occupied`() {
        val goal = singleCountryContinentGoalWithCountries(1)
        val players = twoPlayersWithGoals(goal, goal)
        val gameInfo = createSingleCountryGameInfo(players)
        val occupierInfo = players[1]

        assertTrue(goal.achieved(occupierInfo, gameInfo))
    }

    @Test
    fun `Is achieved when there are countries left in the Continent to occupy but all the necessary countries are occupied`() {
        val firstCountry = "Argentina"
        val secondCountry = "Chile"
        val continent = Continent("América", setOf(firstCountry, secondCountry))
        val politicalMap = PoliticalMap.Builder().addContinent(continent).build()
        val goal = Goal(listOf(OccupySubContinent(continent, 1)))
        val players = twoPlayersWithGoals(goal, goal)
        val occupations = CountryOccupations(
            listOf(
                Occupation(firstCountry, players[0].name, PositiveInt(1)),
                Occupation(secondCountry, players[1].name, PositiveInt(1))
            ))
        val gameInfo = createGameInfo(players, politicalMap, occupations)

        assertTrue(goal.achieved(players[0], gameInfo))
    }

    private fun singleCountryContinentGoalWithCountries(countries: Int) =
        Goal(listOf(OccupySubContinent(SINGLE_COUNTRY_CONTINENT, countries)))
}

class DestroyTest {
    @Test
    fun `Is not achieved if the playerToDestroy is still in the game`() {
        val playerToDestroy = "Nico"
        val goal = Goal(listOf(Destroy(playerToDestroy)))
        val players = twoPlayersWithGoals(goal, goal)
        val gameInfo = createSingleCountryGameInfo(players)

        assertFalse(goal.achieved(players[0], gameInfo))
    }

    @Test
    fun `Is achieved if the playerToDestroy is destroyed by the player`() {
        val playerToDestroy = "Eric"
        val goal = Goal(listOf(Destroy(playerToDestroy)))
        val players = twoPlayersWithGoals(goal, goal)
        val destructions = PlayerDestructions()
        destructions.destroy(playerToDestroy, players[1].name)
        val gameInfo = createSingleCountryGameInfo(players, destructions)

        assertTrue(goal.achieved(players[1], gameInfo))
    }

    @Test
    fun `Is not achieved if the playerToDestroy is destroyed by another player`() {
        val playerToDestroy = "Eric"
        val goal = Goal(listOf(Destroy(playerToDestroy)))
        val players = twoPlayersWithGoals(goal, goal)
        val destructions = PlayerDestructions()
        destructions.destroy(playerToDestroy, "Cristel")
        val gameInfo = createSingleCountryGameInfo(players, destructions)

        assertFalse(goal.achieved(players[1], gameInfo))
    }
}


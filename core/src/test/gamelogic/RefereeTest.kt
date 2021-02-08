package gamelogic

import PositiveInt
import gamelogic.combat.AttackingCountryWinsAttackerFactory
import gamelogic.map.Continent
import gamelogic.map.PoliticalMap
import gamelogic.occupations.CountryOccupations
import gamelogic.occupations.PlayerOccupation
import kotlin.test.*

class RefereeTest {

    private val nico = "Nico"
    private val eric = "Eric"

    private val arg = "Argentina"
    private val bra = "Brasil"
    private val chi = "Chile"
    private val kam = "Kamchatka"
    private val jap = "Japón"
    private val vie = "Vietnam"

    private val sudamerica = Continent("Sudamérica", setOf(arg, bra))
    private val asia = Continent("Asia", setOf(kam, jap))
    private val sudamericaLarge = Continent("Sudamérica", setOf(arg, bra, chi))
    private val asiaLarge = Continent("Asia", setOf(kam, jap, vie))

    private val politicalMap = PoliticalMap.Builder()
        .addContinent(sudamerica)
        .addContinent(asia)
        .addBorder(arg, bra)
        .addBorder(kam, jap)
        .build()

    private val politicalMapLarge = PoliticalMap.Builder()
        .addContinent(sudamericaLarge)
        .addContinent(asiaLarge)
        .addBorder(arg, bra)
        .addBorder(arg, chi)
        .addBorder(kam, jap)
        .addBorder(kam, vie)
        .build()

    private val goalNico = Goal(listOf(OccupyContinent(sudamerica)))
    private val goalEric = Goal(listOf(OccupyContinent(asia)))

    private val players = mutableListOf(
        PlayerInfo(nico, Color.Blue, goalNico),
        PlayerInfo(eric, Color.Brown, goalEric)
    )

    private val occupationsSample = listOf(
        PlayerOccupation(arg, nico, PositiveInt(1)),
        PlayerOccupation(kam, nico, PositiveInt(1)),
        PlayerOccupation(bra, eric, PositiveInt(1)),
        PlayerOccupation(jap, eric, PositiveInt(1))
    )

    private val countryOccupations = CountryOccupations(occupationsSample)

    private val sampleReferee = Referee(players, politicalMap, countryOccupations)


    @Test
    fun `Game is not over if nobody achieved goal`() {
        assertFalse(sampleReferee.gameIsOver)
        assertTrue(sampleReferee.winners.isEmpty())
    }

    @Test
    fun `Nico wins if he plays against eric (and achieves his goal first)`() {
        val occupationsNico = listOf(
            PlayerOccupation(arg, nico, PositiveInt(1)),
            PlayerOccupation(kam, nico, PositiveInt(1)),
            PlayerOccupation(bra, nico, PositiveInt(1))
        )
        val occupationsEric = listOf(PlayerOccupation(jap, eric, PositiveInt(1)))

        val referee = Referee(
            players,
            politicalMap,
            CountryOccupations(occupationsEric.union(occupationsNico))
        )
        assertTrue(referee.gameIsOver)
        assertTrue(referee.winners.contains(nico) and !referee.winners.contains(eric))
    }

    @Test
    fun `Turns change according to list of playerInfo`() {
        assertEquals(sampleReferee.currentPlayer, nico)
    }

    @Test
    fun `AddArmies adds armies and changes referee's state`() {
        val armiesToAdd = PositiveInt(1)
        val reinforcements = listOf(CountryReinforcement(arg, armiesToAdd))
        val armiesBefore = sampleReferee.occupations.armiesOf(arg)
        sampleReferee.addArmies(reinforcements)
        assertEquals(sampleReferee.currentState, Referee.State.Attack)
        assertEquals(
            sampleReferee.occupations.armiesOf(arg), (armiesToAdd + armiesBefore).toInt())
    }

    @Test
    fun `EndAttack ends the attack`() {
        val reinforcements = listOf(CountryReinforcement(arg, PositiveInt(1)))
        sampleReferee.addArmies(reinforcements)
        sampleReferee.endAttack()
        assertEquals(sampleReferee.currentState, Referee.State.Regroup)
    }

    @Test
    fun `Regroup moves the armies and changes turn`() {

        val occupationsSampleLarge = listOf(
            PlayerOccupation(arg, nico, PositiveInt(1)),
            PlayerOccupation(kam, nico, PositiveInt(1)),
            PlayerOccupation(chi, nico, PositiveInt(1)),
            PlayerOccupation(bra, eric, PositiveInt(1)),
            PlayerOccupation(jap, eric, PositiveInt(1)),
            PlayerOccupation(vie, eric, PositiveInt(1))
        )

        val referee = Referee(
            players,
            politicalMapLarge,
            CountryOccupations(occupationsSampleLarge)
        )
        val reinforcements = listOf(CountryReinforcement(arg, PositiveInt(3)))
        referee.addArmies(reinforcements)
        referee.endAttack()
        referee.regroup(listOf(Regrouping(arg, chi, PositiveInt(2))))
        assertEquals(referee.occupations.armiesOf(chi), PositiveInt(3).toInt())
        assertEquals(referee.currentState, Referee.State.AddArmies)
        assertEquals(referee.currentPlayer, eric)
    }

    @Test
    fun `Attacking removes correct amount of armies only from the combating countries`() {

        val occupationsSampleLarge = listOf(
            PlayerOccupation(arg, nico, PositiveInt(1)),
            PlayerOccupation(kam, nico, PositiveInt(1)),
            PlayerOccupation(chi, nico, PositiveInt(1)),
            PlayerOccupation(bra, eric, PositiveInt(1)),
            PlayerOccupation(jap, eric, PositiveInt(1)),
            PlayerOccupation(vie, eric, PositiveInt(1))
        )

        val attackerFactory =
            AttackingCountryWinsAttackerFactory(PositiveInt(4), PositiveInt(1))
        val referee = Referee(
            players,
            politicalMapLarge,
            CountryOccupations(occupationsSampleLarge),
            attackerFactory
        )
        val reinforcements = listOf(CountryReinforcement(arg, PositiveInt(3)))
        referee.addArmies(reinforcements)
        referee.makeAttack(arg, bra)
        assertTrue(referee.occupations.isEmpty(bra))
        assertEquals(referee.occupations.armiesOf(arg), 4)

        referee.endAttack()
        assertEquals(referee.occupations.armiesOf(kam), 1)
        assertEquals(referee.occupations.armiesOf(chi), 1)
        assertEquals(referee.occupations.armiesOf(jap), 1)
        assertEquals(referee.occupations.armiesOf(vie), 1)
    }

    @Test
    fun `Cannot conquer when no country has been conquered`() {
        val occupationsSampleSmall = listOf(
            PlayerOccupation(arg, eric, PositiveInt(1)),
            PlayerOccupation(kam, nico, PositiveInt(1))
        )

        val referee = Referee(
            players, politicalMapLarge, CountryOccupations(occupationsSampleSmall)
        )

        assertFails {
            referee.occupyConqueredCountry(PositiveInt(2))
        }

        assertEquals(1, referee.occupations.armiesOf(arg))
        assertEquals(1, referee.occupations.armiesOf(kam))
    }

    @Test
    fun `Conquering a country moves the requested armies and changes the occupier`() {
        val attackerFactory =
            AttackingCountryWinsAttackerFactory(PositiveInt(4), PositiveInt(1))
        val referee = Referee(players, politicalMap, countryOccupations, attackerFactory)

        referee.addArmies(listOf(CountryReinforcement(arg, PositiveInt(3))))
        referee.makeAttack(arg, bra)
        referee.occupyConqueredCountry(PositiveInt(3))

        assertFalse(referee.occupations.isEmpty(bra))
        assertEquals(nico, referee.occupations.occupierOf(bra))
        assertEquals(3, referee.occupations.armiesOf(bra))
        assertFalse(referee.occupations.isEmpty(arg))
        assertEquals(nico, referee.occupations.occupierOf(arg))
        assertEquals(1, referee.occupations.armiesOf(arg))
    }

}

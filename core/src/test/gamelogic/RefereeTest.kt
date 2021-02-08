package gamelogic

import PositiveInt
import gamelogic.combat.AttackerWinsAttackerFactory
import gamelogic.map.Continent
import gamelogic.map.PoliticalMap
import gamelogic.occupations.CountryOccupations
import gamelogic.occupations.PlayerOccupation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

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

    private val occupationsSample = listOf(
        PlayerOccupation(arg, nico, PositiveInt(1)),
        PlayerOccupation(kam, nico, PositiveInt(1)),
        PlayerOccupation(bra, eric, PositiveInt(1)),
        PlayerOccupation(jap, eric, PositiveInt(1))
    )

    private val sampleReferee = Referee(mutableListOf<PlayerInfo>(PlayerInfo(nico, Color.Blue,goalNico), PlayerInfo(eric, Color.Brown, goalEric)),
        politicalMap,
        CountryOccupations(occupationsSample)
    )


    @Test
    fun `Game is not over if nobody achieved goal`() {
        assertFalse(sampleReferee.gameIsOver)
        assertTrue (sampleReferee.winners.isEmpty())
    }

    @Test
    fun `Nico wins if he plays against eric (and achieves his goal first)`() {
        val occupationsNico = listOf(
            PlayerOccupation(arg, nico, PositiveInt(1)),
            PlayerOccupation(kam, nico, PositiveInt(1)),
            PlayerOccupation(bra, nico, PositiveInt(1))
        )
        val occupationsEric = listOf(PlayerOccupation(jap, eric, PositiveInt(1)))

        val referee = Referee(mutableListOf<PlayerInfo>(PlayerInfo(nico, Color.Blue,goalNico), PlayerInfo(eric, Color.Brown, goalEric)),
            politicalMap,
            CountryOccupations(occupationsEric.union(occupationsNico))
        )
        assertTrue(referee.gameIsOver)
        assertTrue (referee.winners.contains(nico) and !referee.winners.contains(eric))
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

        val referee = Referee(mutableListOf<PlayerInfo>(PlayerInfo(nico, Color.Blue,goalNico), PlayerInfo(eric, Color.Brown, goalEric)),
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

        val players = mutableListOf(
            PlayerInfo(nico, Color.Blue, goalNico),
            PlayerInfo(eric, Color.Brown, goalEric)
        )
        val attackerFactory = AttackerWinsAttackerFactory(PositiveInt(4), PositiveInt(1))
        val referee = Referee(
            players, politicalMapLarge,
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
}

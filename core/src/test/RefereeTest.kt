import gamelogic.*
import gamelogic.map.Continent
import gamelogic.map.PoliticalMap
import gamelogic.occupations.CountryOccupations
import gamelogic.occupations.Occupation
import kotlin.test.*

class RefereeTest {

    val nico = "Nico"
    val eric = "Eric"

    val arg = "Argentina"
    val bra = "Brasil"
    val chi = "Chile"
    val kam = "Kamchatka"
    val jap = "Japón"
    val vie = "Vietnam"

    val sudamerica = Continent("Sudamérica", setOf(arg, bra))
    val asia = Continent("Asia", setOf(kam, jap))
    val sudamericaLarge = Continent("Sudamérica", setOf(arg, bra, chi))
    val asiaLarge = Continent("Asia", setOf(kam, jap, vie))

    val politicalMap = PoliticalMap.Builder()
        .addContinent(sudamerica)
        .addContinent(asia)
        .addBorder(arg, bra)
        .addBorder(kam, jap)
        .build()

    val politicalMapLarge = PoliticalMap.Builder()
        .addContinent(sudamericaLarge)
        .addContinent(asiaLarge)
        .addBorder(arg, bra)
        .addBorder(arg, chi)
        .addBorder(kam, jap)
        .addBorder(kam, vie)
        .build()

    val goalNico = Goal(listOf(OccupyContinent(sudamerica)))
    val goalEric = Goal(listOf(OccupyContinent(asia)))

    val occupationsSample = listOf(
        Occupation(arg, nico, PositiveInt(1)), Occupation(kam, nico, PositiveInt(1)),
        Occupation(bra, eric, PositiveInt(1)), Occupation(jap, eric, PositiveInt(1))
    )

    val sampleReferee = Referee(mutableListOf<PlayerInfo>(PlayerInfo(nico, Color.Blue,goalNico), PlayerInfo(eric, Color.Brown, goalEric)),
        politicalMap,
        CountryOccupations(occupationsSample)
    )


    @Test
    fun `Game is not over if nobody achieved goal`() {
        assertFalse(sampleReferee.gameIsOver())
        assertTrue (sampleReferee.winners().isEmpty())
    }

    @Test
    fun `Nico wins if he plays against eric (and achieve his goal first)`() {
        val occupationsNico = listOf(
            Occupation(arg, nico, PositiveInt(1)), Occupation(kam, nico, PositiveInt(1)),
            Occupation(bra, nico, PositiveInt(1))
        )
        val occupationsEric = listOf(Occupation(jap, eric, PositiveInt(1)))

        val referee = Referee(mutableListOf<PlayerInfo>(PlayerInfo(nico, Color.Blue,goalNico), PlayerInfo(eric, Color.Brown, goalEric)),
            politicalMap,
            CountryOccupations(occupationsEric.union(occupationsNico))
        )
        assertTrue(referee.gameIsOver())
        assertTrue (referee.winners().contains(nico) and !referee.winners().contains(eric))
    }

    @Test
    fun `Turns change according list of playerInfo`() {
        assertTrue  (sampleReferee.currentPlayer() == nico)
    }

    @Test
    fun `AddArmies add armies and change referee's state`() {
        val armiesToAdd = PositiveInt(1)
        val reinforcements = listOf(CountryReinforcement(arg,armiesToAdd) )
        val  armiesBefore = sampleReferee.occupations.armiesOf(arg)
        sampleReferee.addArmies(reinforcements)
        assertTrue (sampleReferee.state == Referee.State.Attack)
        assertTrue (sampleReferee.occupations.armiesOf(arg) == armiesBefore.plus(armiesToAdd))
    }

    @Test
    fun `EndAttack ends the atack`() {
        val reinforcements = listOf(CountryReinforcement(arg, PositiveInt(1)))
        sampleReferee.addArmies(reinforcements)
        sampleReferee.endAttack()
        assertTrue (sampleReferee.state == Referee.State.Regroup)
    }

    @Test
    fun `Regroup move the armies and change turn`() {

        val occupationsSampleLarge = listOf(
            Occupation(arg, nico, PositiveInt(1)), Occupation(kam, nico, PositiveInt(1)), Occupation(chi,nico, PositiveInt(1)),
            Occupation(bra, eric, PositiveInt(1)), Occupation(jap, eric, PositiveInt(1)), Occupation(vie,eric, PositiveInt(1))
        )

        val referee = Referee(mutableListOf<PlayerInfo>(PlayerInfo(nico, Color.Blue,goalNico), PlayerInfo(eric, Color.Brown, goalEric)),
            politicalMapLarge,
            CountryOccupations(occupationsSampleLarge)
        )
        val reinforcements = listOf(CountryReinforcement(arg, PositiveInt(3)))
        referee.addArmies(reinforcements)
        referee.endAttack()
        referee.regroup(listOf(Regrouping(arg, chi, PositiveInt(2), referee)))
        assertTrue  (referee.occupations.armiesOf(chi) == PositiveInt(3))
        assertTrue (referee.state == Referee.State.AddArmies)
        assertTrue (referee.currentPlayer() == eric)
    }

    @Test
    fun `Attack does not change neutral countries`() {

        val occupationsSampleLarge = listOf(
            Occupation(arg, nico, PositiveInt(1)), Occupation(kam, nico, PositiveInt(1)), Occupation(chi,nico, PositiveInt(1)),
            Occupation(bra, eric, PositiveInt(1)), Occupation(jap, eric, PositiveInt(1)), Occupation(vie,eric, PositiveInt(1))
        )

        val referee = Referee(mutableListOf<PlayerInfo>(PlayerInfo(nico, Color.Blue,goalNico), PlayerInfo(eric, Color.Brown, goalEric)),
            politicalMapLarge,
            CountryOccupations(occupationsSampleLarge)
        )
        val reinforcements = listOf(CountryReinforcement(arg, PositiveInt(3)))
        referee.addArmies(reinforcements)
        //TODO: cannot test this until attacker refactor
        //referee.makeAttack(arg, bra)
/*
        if (referee.attackState == Referee.AttackState.Occupation) {
            referee.occupyConqueredCountry(PositiveInt(1))
        }
        referee.endAttack()
*/
        //referee.regroup(listOf(Regrouping(arg, chi, PositiveInt(2), referee)))
        assertTrue(referee.occupations.armiesOf(kam) == PositiveInt(1))
        assertTrue(referee.occupations.armiesOf(chi) == PositiveInt(1))
        assertTrue(referee.occupations.armiesOf(jap) == PositiveInt(1))
        assertTrue(referee.occupations.armiesOf(vie) == PositiveInt(1))
    }

}
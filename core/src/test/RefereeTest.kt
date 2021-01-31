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
    val kam = "Kamchatka"
    val jap = "Japón"

    val sudamerica = Continent("Sudamérica", setOf(arg, bra))
    val asia = Continent("Asia", setOf(kam, jap))

    val politicalMap = PoliticalMap.Builder()
        .addContinent(sudamerica)
        .addContinent(asia)
        .addBorder(arg, bra)
        .addBorder(kam, jap)
        .build()

    val goalNico = Goal(listOf(OccupyContinent(sudamerica)))
    val goalEric = Goal(listOf(OccupyContinent(asia)))

    @Test
    fun `Game is not over if nobody achieved goal`() {
        val occupationsNico = listOf(Occupation(arg, nico, PositiveInt(1)), Occupation(kam, nico, PositiveInt(1)))
        val occupationsEric = listOf(Occupation(bra, eric, PositiveInt(1)), Occupation(jap, eric, PositiveInt(1)))

        val referee = Referee(mutableListOf<PlayerInfo>(PlayerInfo(nico, Color.Blue,goalNico), PlayerInfo(eric, Color.Brown, goalEric)),
            politicalMap,
            CountryOccupations(occupationsEric.union(occupationsNico))
        )
        assertFalse(referee.gameIsOver())
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

}
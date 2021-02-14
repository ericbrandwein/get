package gamelogic

import PositiveInt
import gamelogic.combat.AttackingCountryWinsAttackerFactory
import gamelogic.gameState.CannotEndAttackWhenOccupyingException
import gamelogic.gameState.NotInAttackingStateException
import gamelogic.gameState.NotInRegroupingStateException
import gamelogic.gameState.NotInReinforcingStateException
import gamelogic.map.Continent
import gamelogic.map.PoliticalMap
import gamelogic.occupations.PlayerOccupation
import gamelogic.occupations.dealers.FixedOccupationsDealer
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
    private val playerNames = players.map { it.name }

    private val occupationsSample = listOf(
        PlayerOccupation(arg, nico, PositiveInt(1)),
        PlayerOccupation(kam, nico, PositiveInt(1)),
        PlayerOccupation(bra, eric, PositiveInt(1)),
        PlayerOccupation(jap, eric, PositiveInt(1))
    )

    private val occupationsSampleLarge = listOf(
        PlayerOccupation(arg, nico, PositiveInt(1)),
        PlayerOccupation(kam, nico, PositiveInt(1)),
        PlayerOccupation(chi, nico, PositiveInt(1)),
        PlayerOccupation(bra, eric, PositiveInt(1)),
        PlayerOccupation(jap, eric, PositiveInt(1)),
        PlayerOccupation(vie, eric, PositiveInt(1))
    )

    private val sampleReferee = Referee(
        players,
        politicalMap,
        FixedOccupationsDealer(occupationsSample, playerNames)
    )
    private val sampleRefereeLarge = Referee(
        players,
        politicalMapLarge,
        FixedOccupationsDealer(occupationsSampleLarge, playerNames)
    )

    @Test
    fun `Starting a game deals the countries to the players`() {
        val countries = politicalMap.countries.toList()
        val referee = Referee(players, politicalMap)

        countries.forEach {
            assertTrue(referee.occupations.occupierOf(it) in playerNames)
            assertEquals(1, referee.occupations.armiesOf(it))
        }
    }

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
            FixedOccupationsDealer(occupationsEric.union(occupationsNico), playerNames)
        )
        assertTrue(referee.gameIsOver)
        assertTrue(referee.winners.contains(nico) and !referee.winners.contains(eric))
    }

    @Test
    fun `Turns change according to list of playerInfo`() {
        assertEquals(sampleReferee.currentPlayer, nico)
    }

    @Test
    fun `AddArmies adds armies`() {
        val armiesToAdd = PositiveInt(1)
        val reinforcements = listOf(CountryReinforcement(arg, armiesToAdd))
        val armiesBefore = sampleReferee.occupations.armiesOf(arg)
        sampleReferee.addArmies(reinforcements)
        assertEquals(
            sampleReferee.occupations.armiesOf(arg), (armiesToAdd + armiesBefore).toInt())
    }

    @Test
    fun `Regroup moves the armies and changes turn`() {

        val referee = Referee(
            players,
            politicalMapLarge,
            FixedOccupationsDealer(occupationsSampleLarge, playerNames)
        )
        val reinforcements = listOf(CountryReinforcement(arg, PositiveInt(3)))
        referee.addArmies(reinforcements)
        referee.endAttack()
        referee.regroup(listOf(Regrouping(arg, chi, PositiveInt(2))))
        assertEquals(referee.occupations.armiesOf(chi), 3)
        assertEquals(referee.currentPlayer, eric)
    }

    @Test
    fun `Attacking removes correct amount of armies only from the combating countries`() {

        val attackerFactory =
            AttackingCountryWinsAttackerFactory(PositiveInt(4), PositiveInt(1))
        val referee = Referee(
            players,
            politicalMapLarge,
            FixedOccupationsDealer(occupationsSampleLarge, playerNames),
            attackerFactory
        )
        val reinforcements = listOf(CountryReinforcement(arg, PositiveInt(3)))
        referee.addArmies(reinforcements)
        referee.makeAttack(arg, bra)

        assertTrue(referee.occupations.isEmpty(bra))
        assertEquals(referee.occupations.armiesOf(arg), 4)
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
            players,
            politicalMapLarge,
            FixedOccupationsDealer(occupationsSampleSmall, playerNames)
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
        val referee = Referee(
            players,
            politicalMap,
            FixedOccupationsDealer(occupationsSample, playerNames),
            attackerFactory
        )

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

    @Test
    fun `Cannot attack when not in attacking state`() {
        sampleReferee.addArmies(listOf(CountryReinforcement(arg, PositiveInt(2))))
        sampleReferee.endAttack()
        assertFailsWith<NotInAttackingStateException> {
            sampleReferee.makeAttack(arg, bra)
        }

        assertEquals(3, sampleReferee.occupations.armiesOf(arg))
        assertEquals(1, sampleReferee.occupations.armiesOf(bra))
    }

    @Test
    fun `Cannot attack when occupying a country`() {
        val attackerFactory =
            AttackingCountryWinsAttackerFactory(PositiveInt(4), PositiveInt(1))
        val referee = Referee(
            players,
            politicalMap,
            FixedOccupationsDealer(occupationsSample, playerNames),
            attackerFactory
        )

        referee.addArmies(
            listOf(
                CountryReinforcement(arg, PositiveInt(3)),
                CountryReinforcement(kam, PositiveInt(2))
            ))
        referee.makeAttack(arg, bra)

        assertFails {
            referee.makeAttack(kam, jap)
        }

        assertEquals(4, referee.occupations.armiesOf(arg))
        assertEquals(3, referee.occupations.armiesOf(kam))
        assertEquals(0, referee.occupations.armiesOf(bra))
        assertEquals(1, referee.occupations.armiesOf(jap))
    }

    @Test
    fun `Cannot attack if current player doesn't occupy attacking country`() {
        sampleReferee.addArmies(listOf(CountryReinforcement(arg, PositiveInt(2))))
        sampleReferee.endAttack()
        sampleReferee.regroup(emptyList())
        sampleReferee.addArmies(emptyList())

        assertFails {
            sampleReferee.makeAttack(arg, bra)
        }
    }

    @Test
    fun `Cannot attack if the countries are not bordering`() {
        sampleReferee.addArmies(listOf(CountryReinforcement(arg, PositiveInt(2))))

        val exception = assertFailsWith<CountriesAreNotBorderingException> {
            sampleReferee.makeAttack(arg, jap)
        }

        assertEquals(arg, exception.from)
        assertEquals(jap, exception.to)
        assertEquals(3, sampleReferee.occupations.armiesOf(arg))
        assertEquals(1, sampleReferee.occupations.armiesOf(jap))
    }

    @Test
    fun `Cannot regroup if the countries are not bordering`() {
        sampleReferee.addArmies(listOf(CountryReinforcement(arg, PositiveInt(2))))
        sampleReferee.endAttack()

        val exception = assertFailsWith<CountriesAreNotBorderingException> {
            sampleReferee.regroup(listOf(Regrouping(arg, kam, PositiveInt(2))))
        }

        assertEquals(arg, exception.from)
        assertEquals(kam, exception.to)
        assertEquals(3, sampleReferee.occupations.armiesOf(arg))
        assertEquals(1, sampleReferee.occupations.armiesOf(kam))
    }

    @Test
    fun `Cannot add armies to a country not occupied by the current player`() {
        assertFails {
            sampleReferee.addArmies(listOf(CountryReinforcement(bra, PositiveInt(1))))
        }

        assertEquals(1, sampleReferee.occupations.armiesOf(bra))
    }

    @Test
    fun `Cannot reinforce when not in reinforcing state`() {
        sampleReferee.addArmies(listOf())

        assertFailsWith<NotInReinforcingStateException> {
            sampleReferee.addArmies(listOf())
        }

        politicalMap.countries.forEach { country ->
            assertEquals(1, sampleReferee.occupations.armiesOf(country))
        }
    }

    @Test
    fun `Cannot end attack when occupying`() {
        val referee = Referee(
            players,
            politicalMap,
            FixedOccupationsDealer(occupationsSample, playerNames),
            AttackingCountryWinsAttackerFactory(PositiveInt(4), PositiveInt(1))
        )
        referee.addArmies(listOf(CountryReinforcement(arg, PositiveInt(3))))
        referee.makeAttack(arg, bra)

        assertFailsWith<CannotEndAttackWhenOccupyingException> {
            referee.endAttack()
        }

        assertEquals(4, referee.occupations.armiesOf(arg))
        assertEquals(0, referee.occupations.armiesOf(bra))
    }

    @Test
    fun `Cannot end attack if not attacking`() {
        assertFailsWith<NotInAttackingStateException> {
            sampleReferee.endAttack()
        }
    }

    @Test
    fun `Cannot regroup if not regrouping`() {
        assertFailsWith<NotInRegroupingStateException> {
            sampleReferee.regroup(listOf())
        }
    }
}

package armies

import kotlin.test.*

class ArmyPlacementsTest {

    private val someCountry = "Argentina"
    private val otherCountry = "Algeria"
    private val somePlayer = "Sebastián"
    private val otherPlayer = "Juana"
    private val evenOtherPlayer = "Olivia"
    private val anotherPlayerStill = "John"
    private lateinit var placements: ArmyPlacements

    @BeforeTest
    fun setup() {
        placements = ArmyPlacements()
    }

    @Test
    fun `No country is occupied if no army was added`() {
        assertFalse(placements.isOccupied(someCountry))
    }

    @Test
    fun `Taking a country makes it occupied`() {
        placements.takeCountry(someCountry, player = "Pepe", armies = 1)

        assertTrue(placements.isOccupied(someCountry))
    }

    @Test
    fun `Taking a country does not occupy other countries`() {
        placements.takeCountry(someCountry, player = "Pepe", armies = 1)

        assertFalse(placements.isOccupied(otherCountry))
    }

    @Test
    fun `Can occupy more than one country`() {
        placements.takeCountry(someCountry, player = "Pepe", armies = 1)
        placements.takeCountry(otherCountry, player = "Pepe", armies = 1)

        assertTrue(placements.isOccupied(someCountry))
        assertTrue(placements.isOccupied(otherCountry))
    }

    @Test
    fun `Taking a country occupies for the player`() {
        placements.takeCountry(someCountry, somePlayer, 1)

        assertEquals(somePlayer, placements.occupierFor(someCountry))
    }

    @Test
    fun `There can be different occupiers of different countries`() {
        placements.takeCountry(someCountry, somePlayer, 1)
        placements.takeCountry(otherCountry, otherPlayer, 1)

        assertEquals(somePlayer, placements.occupierFor(someCountry))
        assertEquals(otherPlayer, placements.occupierFor(otherCountry))
    }

    @Test
    fun `Can't ask occupier of unoccupied country`() {
        val exception = assertFailsWith<UnoccupiedCountryException> {
            placements.occupierFor(someCountry)
        }
        assertEquals(someCountry, exception.country)
    }

    @Test
    fun `Unoccupied country has no armies`() {
        assertEquals(0, placements.armiesIn(someCountry))
    }

    @Test
    fun `Taking a country with one army sets one to the quantity of armies in that country`() {
        val quantity = 1

        placements.takeCountry(someCountry, somePlayer, quantity)

        assertEquals(quantity, placements.armiesIn(someCountry))
    }

    @Test
    fun `Taking a country with many armies sets that quantity of armies in that country`() {
        val quantity = 5

        placements.takeCountry(someCountry, somePlayer, quantity)

        assertEquals(quantity, placements.armiesIn(someCountry))
    }

    @Test
    fun `Many countries can have different amounts of armies`() {
        val firstQuantity = 5
        val secondQuantity = 3

        placements.takeCountry(someCountry, somePlayer, firstQuantity)
        placements.takeCountry(otherCountry, somePlayer, secondQuantity)

        assertEquals(firstQuantity, placements.armiesIn(someCountry))
        assertEquals(secondQuantity, placements.armiesIn(otherCountry))
    }

    @Test
    fun `Can't take a country with zero armies`() {
        val armies = 0
        val exception = assertFailsWith<CantOccupyWithNoArmiesException> {
            placements.takeCountry(someCountry, somePlayer, armies)
        }
        assertEquals(armies, exception.amount)
        assertFalse(placements.isOccupied(someCountry))
    }

    @Test
    fun `Can't take a country with negative armies`() {
        val armies = -7
        val exception = assertFailsWith<CantOccupyWithNoArmiesException> {
            placements.takeCountry(someCountry, somePlayer, armies)
        }
        assertEquals(armies, exception.amount)
        assertFalse(placements.isOccupied(someCountry))
    }

    @Test
    fun `Same player can't take a country if already taken by him`() {
        placements.takeCountry(someCountry, somePlayer, 1)

        val exception = assertFailsWith<CountryAlreadyOccupiedByPlayerException> {
            placements.takeCountry(someCountry, somePlayer, 1)
        }
        assertEquals(someCountry, exception.country)
        assertEquals(somePlayer, exception.player)
        assertEquals(somePlayer, placements.occupierFor(someCountry))
        assertEquals(1, placements.armiesIn(someCountry))
    }

    @Test
    fun `Adding armies to country occupied by player adds to the armies in that country`() {
        placements.takeCountry(someCountry, somePlayer, 1)
        placements.addArmies(someCountry, 2)

        assertEquals(3, placements.armiesIn(someCountry))
    }

    @Test
    fun `Can't add a negative amount of armies to a country`() {
        placements.takeCountry(someCountry, somePlayer, 1)
        val added = -2

        val exception = assertFailsWith<NonPositiveAmountAddedException> {
            placements.addArmies(someCountry, added)
        }
        assertEquals(added, exception.amount)
    }

    @Test
    fun `Can't add zero armies to a country`() {
        placements.takeCountry(someCountry, somePlayer, 1)
        val added = 0

        val exception = assertFailsWith<NonPositiveAmountAddedException> {
            placements.addArmies(someCountry, added)
        }
        assertEquals(added, exception.amount)
    }

    @Test
    fun `Can't add armies to an unoccupied country`() {
        val exception = assertFailsWith<UnoccupiedCountryException> {
            placements.addArmies(someCountry, 2)
        }
        assertEquals(someCountry, exception.country)
        assertEquals(0, placements.armiesIn(someCountry))
    }

    @Test
    fun `Removing armies substracts from the amount of armies in the country`() {
        placements.takeCountry(someCountry, somePlayer, 2)
        placements.removeArmies(someCountry, 1)

        assertEquals(1, placements.armiesIn(someCountry))
    }

    @Test
    fun `Can't remove armies from an unoccupied country`() {
        val exception = assertFailsWith<UnoccupiedCountryException> {
            placements.removeArmies(someCountry, 1)
        }
        assertEquals(someCountry, exception.country)
        assertFalse(placements.isOccupied(someCountry))
    }

    @Test
    fun `Can't remove more armies than there are in the country`() {
        val originalAmount = 2
        placements.takeCountry(someCountry, "Santiago", originalAmount)
        val amountRemoved = 3

        val exception = assertFailsWith<CantRemoveMoreArmiesThanAvailableException> {
            placements.removeArmies(someCountry, amountRemoved)
        }
        assertEquals(someCountry, exception.country)
        assertEquals(originalAmount, exception.originalAmount)
        assertEquals(amountRemoved, exception.amountRemoved)
        assertEquals(originalAmount, placements.armiesIn(someCountry))
    }

    @Test
    fun `Can't remove all armies from a country`() {
        val armies = 2
        placements.takeCountry(someCountry, "Santiago", armies)

        val exception = assertFailsWith<CantRemoveMoreArmiesThanAvailableException> {
            placements.removeArmies(someCountry, armies)
        }
        assertEquals(someCountry, exception.country)
        assertEquals(armies, exception.originalAmount)
        assertEquals(armies, exception.amountRemoved)
        assertEquals(armies, placements.armiesIn(someCountry))
    }

    @Test
    fun `Can't remove a negative amount of armies`() {
        val originalArmies = 3
        placements.takeCountry(someCountry, "Santiago", originalArmies)

        val removedArmies = -2
        val exception = assertFailsWith<NonPositiveAmountRemovedException> {
            placements.removeArmies(someCountry, removedArmies)
        }
        assertEquals(removedArmies, exception.amountRemoved)
        assertEquals(originalArmies, placements.armiesIn(someCountry))
    }

    @Test
    fun `Can't remove zero armies from a country`() {
        val originalArmies = 3
        placements.takeCountry(someCountry, "Santiago", originalArmies)

        val removedArmies = 0
        val exception = assertFailsWith<NonPositiveAmountRemovedException> {
            placements.removeArmies(someCountry, removedArmies)
        }
        assertEquals(removedArmies, exception.amountRemoved)
        assertEquals(originalArmies, placements.armiesIn(someCountry))
    }

    @Test
    fun `Taking a country and sharing it makes it occupied`() {
        placements.takeCountry(someCountry, "first", "second", 1, 2)

        assertTrue(placements.isOccupied(someCountry))
    }

    @Test
    fun `Armies in a shared country returns the sum of both armies`() {
        val firstArmies = 1
        val secondArmies = 2

        placements.takeCountry(someCountry, "first", "second", firstArmies, secondArmies)

        assertEquals(firstArmies + secondArmies, placements.armiesIn(someCountry))
    }

    @Test
    fun `Taking a country and sharing it makes its occupiers be the takers`() {
        val firstArmies = 4
        val secondArmies = 3

        placements.takeCountry(
            someCountry, somePlayer, otherPlayer, firstArmies, secondArmies)

        val occupiers = placements.occupiersFor(someCountry)
        assertEquals(2, occupiers.size)
        assertTrue(occupiers.contains(somePlayer))
        assertTrue(occupiers.contains(otherPlayer))
    }

    @Test
    fun `Unoccupied country has no occupiers`() {
        assertTrue(placements.occupiersFor(someCountry).isEmpty())
    }

    @Test
    fun `Country occupied by only one player has only that player in occupiers`() {
        placements.takeCountry(someCountry, somePlayer, 1)

        val occupiers = placements.occupiersFor(someCountry)
        assertEquals(1, occupiers.size)
        assertEquals(somePlayer, occupiers.single())
    }

    @Test
    fun `Country taken by two players has no single occupier`() {
        placements.takeCountry(someCountry, "Alice", "Bob", 1, 2)

        val exception = assertFailsWith<NoSingleOccupierException> {
            placements.occupierFor(someCountry)
        }
        assertEquals(someCountry, exception.country)
    }

    @Test
    fun `Can't take a country and share it with a non positive amount of armies for the first occupier`() {
        val armies = 0
        val exception = assertFailsWith<CantOccupyWithNoArmiesException> {
            placements.takeCountry(someCountry, somePlayer, otherPlayer, armies, 3)
        }
        assertEquals(armies, exception.amount)
        assertFalse(placements.isOccupied(someCountry))
    }

    @Test
    fun `Can't take a country and share it with a non positive amount of armies for the second occupier`() {
        val armies = -3
        val exception = assertFailsWith<CantOccupyWithNoArmiesException> {
            placements.takeCountry(someCountry, somePlayer, otherPlayer, 4, armies)
        }
        assertEquals(armies, exception.amount)
        assertFalse(placements.isOccupied(someCountry))
    }

    @Test
    fun `Can't add armies to shared country without specifying player`() {
        val firstArmies = 2
        val secondArmies = 10

        placements.takeCountry(someCountry, "Alice", "Bob", firstArmies, secondArmies)

        val exception = assertFailsWith<NoSingleOccupierException> {
            placements.addArmies(someCountry, 2)
        }
        assertEquals(someCountry, exception.country)
        assertEquals(firstArmies + secondArmies, placements.armiesIn(someCountry))
    }

    @Test
    fun `Can't remove armies from shared country without specifying player`() {
        val firstArmies = 2
        val secondArmies = 10

        placements.takeCountry(someCountry, "Alice", "Bob", firstArmies, secondArmies)

        val exception = assertFailsWith<NoSingleOccupierException> {
            placements.removeArmies(someCountry, 2)
        }
        assertEquals(someCountry, exception.country)
        assertEquals(firstArmies + secondArmies, placements.armiesIn(someCountry))
    }

    @Test
    fun `Taking with two players a country occupied by a single player makes it occupied by the new players`() {
        placements.takeCountry(someCountry, "Vissio", 1)
        placements.takeCountry(someCountry, somePlayer, otherPlayer, 1, 2)

        val occupiers = placements.occupiersFor(someCountry)
        assertEquals(2, occupiers.size)
        assertTrue(occupiers.contains(somePlayer))
        assertTrue(occupiers.contains(otherPlayer))
    }

    @Test
    fun `Can't take with two players a country occupied by the first player`() {
        placements.takeCountry(someCountry, somePlayer, 1)

        val exception = assertFailsWith<CountryAlreadyOccupiedByPlayerException> {
            placements.takeCountry(someCountry, somePlayer, otherPlayer, 1, 2)
        }
        assertEquals(someCountry, exception.country)
        assertEquals(somePlayer, exception.player)
        assertEquals(somePlayer, placements.occupierFor(someCountry))
    }

    @Test
    fun `Can't take with two players a country occupied by the second player`() {
        placements.takeCountry(someCountry, otherPlayer, 1)

        val exception = assertFailsWith<CountryAlreadyOccupiedByPlayerException> {
            placements.takeCountry(someCountry, somePlayer, otherPlayer, 1, 2)
        }
        assertEquals(someCountry, exception.country)
        assertEquals(otherPlayer, exception.player)
        assertEquals(otherPlayer, placements.occupierFor(someCountry))
    }

    @Test
    fun `Can't take with two players a country that is being shared by the first player`() {
        placements.takeCountry(someCountry, somePlayer, otherPlayer, 1, 2)

        val exception = assertFailsWith<CountryAlreadyOccupiedByPlayerException> {
            placements.takeCountry(someCountry, somePlayer, evenOtherPlayer, 1, 2)
        }
        assertEquals(someCountry, exception.country)
        assertEquals(somePlayer, exception.player)
        assertEquals(setOf(somePlayer, otherPlayer), placements.occupiersFor(someCountry))
    }

    @Test
    fun `Can't take with two players a country that is being shared by the second player`() {
        placements.takeCountry(someCountry, somePlayer, otherPlayer, 1, 2)

        val exception = assertFailsWith<CountryAlreadyOccupiedByPlayerException> {
            placements.takeCountry(someCountry, evenOtherPlayer, otherPlayer, 1, 2)
        }
        assertEquals(someCountry, exception.country)
        assertEquals(otherPlayer, exception.player)
        assertEquals(setOf(somePlayer, otherPlayer), placements.occupiersFor(someCountry))
    }

    @Test
    fun `One of the sharing players can take the country from the other one`() {
        placements.takeCountry(someCountry, somePlayer, otherPlayer, 1, 2)

        placements.takeCountry(someCountry, otherPlayer, 3)

        assertEquals(otherPlayer, placements.occupierFor(someCountry))
    }

    @Test
    fun `Can ask for the armies of the first occupier in a shared country`() {
        val armies = 2

        placements.takeCountry(someCountry, somePlayer, otherPlayer, armies, 2)

        assertEquals(armies, placements.armiesIn(someCountry, somePlayer))
    }

    @Test
    fun `Can ask for the armies of the second occupier in a shared country`() {
        val armies = 6

        placements.takeCountry(someCountry, somePlayer, otherPlayer, 4, armies)

        assertEquals(armies, placements.armiesIn(someCountry, otherPlayer))
    }

    @Test
    fun `Can't ask for the armies of a player that isn't occupying a shared country`() {
        val armies = 6

        placements.takeCountry(someCountry, somePlayer, otherPlayer, 4, armies)

        val exception = assertFailsWith<PlayerIsNotOccupyingCountryException> {
            placements.armiesIn(someCountry, evenOtherPlayer)
        }
        assertEquals(evenOtherPlayer, exception.player)
        assertEquals(someCountry, exception.country)
        assertEquals(setOf(somePlayer, otherPlayer), placements.occupiersFor(someCountry))
    }

    @Test
    fun `Can ask for the armies of the single player occupying a country`() {
        val armies = 6

        placements.takeCountry(someCountry, somePlayer, armies)

        assertEquals(armies, placements.armiesIn(someCountry, somePlayer))
    }

    @Test
    fun `Can't ask for the armies of a player that isn't the sole occupier of a country`() {
        val armies = 6

        placements.takeCountry(someCountry, somePlayer, armies)

        val exception = assertFailsWith<PlayerIsNotOccupyingCountryException> {
            placements.armiesIn(someCountry, otherPlayer)
        }
        assertEquals(someCountry, exception.country)
        assertEquals(otherPlayer, exception.player)
    }

    @Test
    fun `Can't ask for the armies of a player for an unoccupied country`() {
        val exception = assertFailsWith<PlayerIsNotOccupyingCountryException> {
            placements.armiesIn(someCountry, somePlayer)
        }
        assertEquals(someCountry, exception.country)
        assertEquals(somePlayer, exception.player)
    }

    @Test
    fun `Can add armies to one of the occupiers in a shared country`() {
        placements.takeCountry(someCountry, somePlayer, otherPlayer, 1, 2)
        placements.addArmies(someCountry, 3, somePlayer)

        assertEquals(4, placements.armiesIn(someCountry, somePlayer))
        assertEquals(6, placements.armiesIn(someCountry))
    }

    @Test
    fun `Can't add armies to a non occupying player in a shared country`() {
        placements.takeCountry(someCountry, somePlayer, otherPlayer, 1, 2)

        val exception = assertFailsWith<PlayerIsNotOccupyingCountryException> {
            placements.addArmies(someCountry, 4, evenOtherPlayer)
        }
        assertEquals(someCountry, exception.country)
        assertEquals(evenOtherPlayer, exception.player)
    }

    @Test
    fun `Can add armies of the sole occupier of a country to that country`() {
        placements.takeCountry(someCountry, somePlayer, 4)
        placements.addArmies(someCountry, 6, somePlayer)

        assertEquals(10, placements.armiesIn(someCountry, somePlayer))
    }

    @Test
    fun `Can't add armies of other player to a country with a single occupier`() {
        placements.takeCountry(someCountry, somePlayer, 4)

        val exception = assertFailsWith<PlayerIsNotOccupyingCountryException> {
            placements.addArmies(someCountry, 6, otherPlayer)
        }

        assertEquals(someCountry, exception.country)
        assertEquals(otherPlayer, exception.player)
        assertEquals(4, placements.armiesIn(someCountry, somePlayer))
    }

    @Test
    fun `Can't add armies of any player to an unoccupied country`() {
        val exception = assertFailsWith<PlayerIsNotOccupyingCountryException> {
            placements.addArmies(someCountry, 6, somePlayer)
        }

        assertEquals(someCountry, exception.country)
        assertEquals(somePlayer, exception.player)
        assertFalse(placements.isOccupied(someCountry))
    }

    @Test
    fun `Can't add a non positive amount of armies to a player in a country`() {
        val originalArmies = 1

        placements.takeCountry(someCountry, somePlayer, "Gato", originalArmies, 2)

        val removed = -6
        val exception = assertFailsWith<NonPositiveAmountAddedException> {
            placements.addArmies(someCountry, removed, somePlayer)
        }

        assertEquals(removed, exception.amount)
        assertEquals(originalArmies, placements.armiesIn(someCountry, somePlayer))
    }

    @Test
    fun `Removing armies from a player in a shared country subtracts from the amount of armies`() {
        val armies = 5
        placements.takeCountry(someCountry, somePlayer, otherPlayer, armies, 4)

        placements.removeArmies(someCountry, 3, somePlayer)

        assertEquals(2, placements.armiesIn(someCountry, somePlayer))
    }

    @Test
    fun `Can't remove more armies from a player than what they have`() {
        val armies = 1
        placements.takeCountry(someCountry, somePlayer, otherPlayer, armies, 4)

        val removed = 3
        val exception = assertFailsWith<CantRemoveMoreArmiesThanAvailableException> {
            placements.removeArmies(someCountry, removed, somePlayer)
        }
        assertEquals(someCountry, exception.country)
        assertEquals(armies, exception.originalAmount)
        assertEquals(removed, exception.amountRemoved)
        assertEquals(armies, placements.armiesIn(someCountry, somePlayer))
    }

    @Test
    fun `Can't remove a non positive amount of armies from a player`() {
        val armies = 3
        placements.takeCountry(someCountry, somePlayer, otherPlayer, armies, 4)

        val removed = -3
        val exception = assertFailsWith<NonPositiveAmountRemovedException> {
            placements.removeArmies(someCountry, removed, somePlayer)
        }
        assertEquals(removed, exception.amountRemoved)
        assertEquals(armies, placements.armiesIn(someCountry, somePlayer))
    }

    @Test
    fun `Can remove armies from the single player occupying a country`() {
        val armies = 3
        placements.takeCountry(someCountry, somePlayer, armies)

        val removed = 2
        placements.removeArmies(someCountry, removed, somePlayer)

        assertEquals(armies - removed, placements.armiesIn(someCountry, somePlayer))
    }

    @Test
    fun `Can't remove armies from other than the single player occupying a country`() {
        val armies = 3
        placements.takeCountry(someCountry, somePlayer, armies)

        val exception = assertFailsWith<PlayerIsNotOccupyingCountryException> {
            placements.removeArmies(someCountry, 2, otherPlayer)
        }
        assertEquals(someCountry, exception.country)
        assertEquals(otherPlayer, exception.player)
        assertEquals(armies, placements.armiesIn(someCountry, somePlayer))
    }

    @Test
    fun `Can't remove armies any player in an unoccupied country`() {
        val exception = assertFailsWith<PlayerIsNotOccupyingCountryException> {
            placements.removeArmies(someCountry, 2, somePlayer)
        }
        assertEquals(someCountry, exception.country)
        assertEquals(somePlayer, exception.player)
        assertFalse(placements.isOccupied(someCountry))
    }

    @Test
    fun `Can replace a player in a shared country`() {
        placements.takeCountry(someCountry, somePlayer, otherPlayer, 1, 2)

        val armies = 3
        placements.takeShare(someCountry, somePlayer, evenOtherPlayer, armies)

        assertEquals(
            setOf(otherPlayer, evenOtherPlayer),
            placements.occupiersFor(someCountry)
        )
        assertEquals(armies, placements.armiesIn(someCountry, evenOtherPlayer))
    }

    @Test
    fun `Can't replace a non existent player in a shared country`() {
        placements.takeCountry(someCountry, somePlayer, otherPlayer, 1, 2)

        val exception = assertFailsWith<PlayerIsNotOccupyingCountryException> {
            placements.takeShare(someCountry, evenOtherPlayer, anotherPlayerStill, 3)
        }

        assertEquals(someCountry, exception.country)
        assertEquals(evenOtherPlayer, exception.player)
        assertEquals(setOf(somePlayer, otherPlayer), placements.occupiersFor(someCountry))
    }

    @Test
    fun `Player can't occupy a share if already occupying the country`() {
        placements.takeCountry(someCountry, somePlayer, otherPlayer, 1, 2)

        val exception = assertFailsWith<CountryAlreadyOccupiedByPlayerException> {
            placements.takeShare(someCountry, somePlayer, otherPlayer, 3)
        }
        assertEquals(someCountry, exception.country)
        assertEquals(otherPlayer, exception.player)
        assertEquals(setOf(somePlayer, otherPlayer), placements.occupiersFor(someCountry))
    }

    @Test
    fun `Can't take a share from a country occupied by a single player`() {
        placements.takeCountry(someCountry, somePlayer, 2)

        val exception = assertFailsWith<CountryNotSharedException> {
            placements.takeShare(someCountry, somePlayer, otherPlayer, 3)
        }
        assertEquals(someCountry, exception.country)
        assertEquals(somePlayer, placements.occupierFor(someCountry))
    }

    @Test
    fun `Can't take a share from an unoccupied country`() {
        val exception = assertFailsWith<CountryNotSharedException> {
            placements.takeShare(someCountry, somePlayer, otherPlayer, 3)
        }
        assertEquals(someCountry, exception.country)
        assertFalse(placements.isOccupied(someCountry))
    }

    @Test
    fun `Can't take a share with a non positive amount of countries`() {
        placements.takeCountry(someCountry, somePlayer, otherPlayer, 1, 2)

        val armies = 0
        val exception = assertFailsWith<CantOccupyWithNoArmiesException> {
            placements.takeShare(someCountry, somePlayer, evenOtherPlayer, armies)
        }
        assertEquals(armies, exception.amount)
        assertEquals(setOf(somePlayer, otherPlayer), placements.occupiersFor(someCountry))
    }

    @Test
    fun `Country taken by two players is shared`() {
        placements.takeCountry(someCountry, somePlayer, otherPlayer, 1, 2)

        assertTrue(placements.isShared(someCountry))
    }

    @Test
    fun `Unoccupied country is not shared`() {
        assertFalse(placements.isShared(someCountry))
    }

    @Test
    fun `Country with single occupier is not shared`() {
        placements.takeCountry(someCountry, somePlayer, 1)

        assertFalse(placements.isShared(someCountry))
    }
}

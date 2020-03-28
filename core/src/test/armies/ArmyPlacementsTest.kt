package armies

import kotlin.test.*

class ArmyPlacementsTest {
    @Test
    fun `No country is occupied if no army was added`() {
        val country = "Argentina"
        val placements = ArmyPlacements()

        assertFalse(placements.isOccupied(country))
    }

    @Test
    fun `Taking a country makes it occupied`() {
        val country = "Argentina"
        val placements = ArmyPlacements()
        placements.takeCountry(country, player = "Pepe", armies = 1)

        assertTrue(placements.isOccupied(country))
    }

    @Test
    fun `Taking a country does not occupy other countries`() {
        val firstCountry = "Argentina"
        val secondCountry = "Uruguay"
        val placements = ArmyPlacements()
        placements.takeCountry(firstCountry, player = "Pepe", armies = 1)

        assertFalse(placements.isOccupied(secondCountry))
    }

    @Test
    fun `Can occupy more than one country`() {
        val firstCountry = "Argentina"
        val secondCountry = "Uruguay"
        val placements = ArmyPlacements()
        placements.takeCountry(firstCountry, player = "Pepe", armies = 1)
        placements.takeCountry(secondCountry, player = "Pepe", armies = 1)

        assertTrue(placements.isOccupied(firstCountry))
        assertTrue(placements.isOccupied(secondCountry))
    }

    @Test
    fun `Taking a country occupies for the player`() {
        val country = "Argentina"
        val player = "Pepe"
        val placements = ArmyPlacements()
        placements.takeCountry(country, player, 1)

        assertEquals(player, placements.occupierFor(country))
    }

    @Test
    fun `There can be different occupiers of different countries`() {
        val firstCountry = "Argentina"
        val secondCountry = "Uruguay"
        val firstPlayer = "Pepe"
        val secondPlayer = "Lulu"
        val placements = ArmyPlacements()
        placements.takeCountry(firstCountry, firstPlayer, 1)
        placements.takeCountry(secondCountry, secondPlayer, 1)

        assertEquals(firstPlayer, placements.occupierFor(firstCountry))
        assertEquals(secondPlayer, placements.occupierFor(secondCountry))
    }

    @Test
    fun `Can't ask occupier of unoccupied country`() {
        val country = "Argentina"
        val placements = ArmyPlacements()

        val exception = assertFailsWith<UnoccupiedCountryException> {
            placements.occupierFor(country)
        }
        assertEquals(country, exception.country)
    }

    @Test
    fun `Unoccupied country has no armies`() {
        val country = "Argentina"
        val placements = ArmyPlacements()

        assertEquals(0, placements.armiesIn(country))
    }

    @Test
    fun `Taking a country with one army sets one to the quantity of armies in that country`() {
        val country = "Argentina"
        val player = "Pepe"
        val quantity = 1
        val placements = ArmyPlacements()
        placements.takeCountry(country, player, quantity)

        assertEquals(quantity, placements.armiesIn(country))
    }

    @Test
    fun `Taking a country with many armies sets that quantity of armies in that country`() {
        val country = "Argentina"
        val player = "Pepe"
        val quantity = 5
        val placements = ArmyPlacements()
        placements.takeCountry(country, player, quantity)

        assertEquals(quantity, placements.armiesIn(country))
    }

    @Test
    fun `Many countries can have different amounts of armies`() {
        val firstCountry = "Argentina"
        val secondCountry = "Uruguay"
        val player = "Pepe"
        val firstQuantity = 5
        val secondQuantity = 3
        val placements = ArmyPlacements()
        placements.takeCountry(firstCountry, player, firstQuantity)
        placements.takeCountry(secondCountry, player, secondQuantity)

        assertEquals(firstQuantity, placements.armiesIn(firstCountry))
        assertEquals(secondQuantity, placements.armiesIn(secondCountry))
    }

    @Test
    fun `Can't take a country with zero armies`() {
        val country = "Argentina"
        val player = "Pepe"
        val placements = ArmyPlacements()

        val armies = 0
        val exception = assertFailsWith<CantOccupyWithNoArmiesException> {
            placements.takeCountry(country, player, armies)
        }
        assertEquals(armies, exception.amount)
        assertFalse(placements.isOccupied(country))
    }

    @Test
    fun `Can't take a country with negative armies`() {
        val country = "Argentina"
        val player = "Pepe"
        val placements = ArmyPlacements()

        val armies = -7
        val exception = assertFailsWith<CantOccupyWithNoArmiesException> {
            placements.takeCountry(country, player, armies)
        }
        assertEquals(armies, exception.amount)
        assertFalse(placements.isOccupied(country))
    }

    @Test
    fun `Same player can't take a country if already taken by him`() {
        val country = "Argentina"
        val player = "Alex"
        val placements = ArmyPlacements()
        placements.takeCountry(country, player, 1)

        val exception = assertFailsWith<CountryAlreadyOccupiedByPlayerException> {
            placements.takeCountry(country, player, 1)
        }
        assertEquals(country, exception.country)
        assertEquals(player, exception.player)
        assertEquals(player, placements.occupierFor(country))
        assertEquals(1, placements.armiesIn(country))
    }

    @Test
    fun `Adding armies to country occupied by player adds to the armies in that country`() {
        val country = "Argentina"
        val player = "Alex"
        val placements = ArmyPlacements()
        placements.takeCountry(country, player, 1)
        placements.addArmies(country, 2)

        assertEquals(3, placements.armiesIn(country))
    }

    @Test
    fun `Can't add a negative amount of armies to a country`() {
        val country = "Argentina"
        val player = "Alex"
        val placements = ArmyPlacements()
        placements.takeCountry(country, player, 1)
        val added = -2

        val exception = assertFailsWith<NonPositiveAmountAddedException> {
            placements.addArmies(country, added)
        }
        assertEquals(added, exception.amount)
    }

    @Test
    fun `Can't add zero armies to a country`() {
        val country = "Argentina"
        val player = "Alex"
        val placements = ArmyPlacements()
        placements.takeCountry(country, player, 1)
        val added = 0

        val exception = assertFailsWith<NonPositiveAmountAddedException> {
            placements.addArmies(country, added)
        }
        assertEquals(added, exception.amount)
    }

    @Test
    fun `Can't add armies to an unoccupied country`() {
        val placements = ArmyPlacements()
        val country = "Russia"

        val exception = assertFailsWith<UnoccupiedCountryException> {
            placements.addArmies(country, 2)
        }
        assertEquals(country, exception.country)
        assertEquals(0, placements.armiesIn(country))
    }

    @Test
    fun `Removing armies substracts from the amount of armies in the country`() {
        val country = "Belarus"
        val player = "Esteban"
        val placements = ArmyPlacements()

        placements.takeCountry(country, player, 2)
        placements.removeArmies(country, 1)

        assertEquals(1, placements.armiesIn(country))
    }

    @Test
    fun `Can't remove armies from an unoccupied country`() {
        val country = "Belarus"
        val placements = ArmyPlacements()

        val exception = assertFailsWith<UnoccupiedCountryException> {
            placements.removeArmies(country, 1)
        }
        assertEquals(country, exception.country)
        assertFalse(placements.isOccupied(country))
    }

    @Test
    fun `Can't remove more armies than there are in the country`() {
        val country = "Belarus"
        val placements = ArmyPlacements()

        val originalAmount = 2
        placements.takeCountry(country, "Santiago", originalAmount)
        val amountRemoved = 3

        val exception = assertFailsWith<CantRemoveMoreArmiesThanAvailableException> {
            placements.removeArmies(country, amountRemoved)
        }
        assertEquals(country, exception.country)
        assertEquals(originalAmount, exception.originalAmount)
        assertEquals(amountRemoved, exception.amountRemoved)
        assertEquals(originalAmount, placements.armiesIn(country))
    }

    @Test
    fun `Can't remove all armies from a country`() {
        val country = "Belarus"
        val placements = ArmyPlacements()

        val armies = 2
        placements.takeCountry(country, "Santiago", armies)

        val exception = assertFailsWith<CantRemoveMoreArmiesThanAvailableException> {
            placements.removeArmies(country, armies)
        }
        assertEquals(country, exception.country)
        assertEquals(armies, exception.originalAmount)
        assertEquals(armies, exception.amountRemoved)
        assertEquals(armies, placements.armiesIn(country))
    }

    @Test
    fun `Can't remove a negative amount of armies`() {
        val country = "Belarus"
        val placements = ArmyPlacements()

        val originalArmies = 3
        placements.takeCountry(country, "Santiago", originalArmies)

        val removedArmies = -2
        val exception = assertFailsWith<NonPositiveAmountRemovedException> {
            placements.removeArmies(country, removedArmies)
        }
        assertEquals(removedArmies, exception.amountRemoved)
        assertEquals(originalArmies, placements.armiesIn(country))
    }

    @Test
    fun `Can't remove zero armies from a country`() {
        val country = "Belarus"
        val placements = ArmyPlacements()

        val originalArmies = 3
        placements.takeCountry(country, "Santiago", originalArmies)

        val removedArmies = 0
        val exception = assertFailsWith<NonPositiveAmountRemovedException> {
            placements.removeArmies(country, removedArmies)
        }
        assertEquals(removedArmies, exception.amountRemoved)
        assertEquals(originalArmies, placements.armiesIn(country))
    }
}
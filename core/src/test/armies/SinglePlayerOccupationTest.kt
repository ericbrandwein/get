package armies

import kotlin.test.*

class SinglePlayerOccupationTest {

    private val somePlayer = "Juancito"
    private val otherPlayer = "Adolfa"
    private val anotherPlayer = "Gronk"

    @Test
    fun `SinglePlayerOccupation means it's occupied`() {
        val occupation = SinglePlayerOccupation(somePlayer, 2)
        assertTrue(occupation.isOccupied())
    }

    @Test
    fun `SinglePlayerOccupation is not shared`() {
        val occupation = SinglePlayerOccupation(somePlayer, 2)
        assertFalse(occupation.isShared())
    }

    @Test
    fun `Creating a SinglePlayerOccupation sets the required members`() {
        val armies = 3
        val occupation = SinglePlayerOccupation(somePlayer, armies)

        assertEquals(somePlayer, occupation.occupier)
        assertEquals(armies, occupation.armies)
    }

    @Test
    fun `Can't create a SinglePlayerOccupation with a negative amount of armies`() {
        val armies = -1
        val exception = assertFailsWith<NonPositiveArmiesException> {
            SinglePlayerOccupation(somePlayer, armies)
        }

        assertEquals(armies, exception.armies)
    }

    @Test
    fun `Can't create a SinglePlayerOccupation with a zero armies`() {
        val armies = 0
        val exception = assertFailsWith<NonPositiveArmiesException> {
            SinglePlayerOccupation(somePlayer, armies)
        }

        assertEquals(armies, exception.armies)
    }

    @Test
    fun `Occupying a SinglePlayerOccupation with one other player returns an occupation with only that player`() {
        val armies = 3
        val occupation = SinglePlayerOccupation(somePlayer, 4)

        val newOccupation = occupation.occupy(otherPlayer, armies)

        assertEquals(otherPlayer, newOccupation.occupier)
        assertEquals(armies, newOccupation.armies)
    }

    @Test
    fun `Can't occupy a country with the same player that is already occupying it`() {
        val armies = 3
        val occupation = SinglePlayerOccupation(somePlayer, armies)

        val exception = assertFailsWith<PlayerAlreadyOccupiesCountryException> {
            occupation.occupy(somePlayer, armies)
        }
        assertEquals(somePlayer, exception.player)
    }

    @Test
    fun `Occupying a SinglePlayerOccupation with two players returns an occupation with the two players as occupiers`() {
        val firstArmies = 1
        val secondArmies = 2
        val occupation = SinglePlayerOccupation(somePlayer, 3)

        val newOccupation =
            occupation.occupy(otherPlayer, firstArmies, anotherPlayer, secondArmies)

        assertEquals(setOf(otherPlayer, anotherPlayer), newOccupation.occupiers)
        assertEquals(firstArmies, newOccupation.armiesOf(otherPlayer))
        assertEquals(secondArmies, newOccupation.armiesOf(anotherPlayer))
    }

    @Test
    fun `Can't occupy with two players if the first is the same one currently occupying`() {
        val occupation = SinglePlayerOccupation(somePlayer, 3)

        val exception = assertFailsWith<PlayerAlreadyOccupiesCountryException> {
            occupation.occupy(somePlayer, 1, otherPlayer, 2)
        }
        assertEquals(somePlayer, exception.player)
    }

    @Test
    fun `Can't occupy with two players if the second is the same one currently occupying`() {
        val occupation = SinglePlayerOccupation(somePlayer, 3)

        val exception = assertFailsWith<PlayerAlreadyOccupiesCountryException> {
            occupation.occupy(otherPlayer, 1, somePlayer, 2)
        }
        assertEquals(somePlayer, exception.player)
    }

    @Test
    fun `Adding armies to a SinglePlayerOccupation adds to the amount of armies`() {
        val occupation = SinglePlayerOccupation(somePlayer, 3)

        occupation.addArmies(4)

        assertEquals(7, occupation.armies)
    }

    @Test
    fun `Can't add a negative amount to armies`() {
        val originalArmies = 3
        val occupation = SinglePlayerOccupation(somePlayer, originalArmies)

        val added = -2
        val exception = assertFailsWith<NonPositiveArmiesAddedException> {
            occupation.addArmies(added)
        }

        assertEquals(added, exception.armies)
        assertEquals(originalArmies, occupation.armies)
    }

    @Test
    fun `Can't add zero armies`() {
        val occupation = SinglePlayerOccupation(somePlayer, 3)

        val added = 0
        val exception = assertFailsWith<NonPositiveArmiesAddedException> {
            occupation.addArmies(added)
        }

        assertEquals(added, exception.armies)
    }

    @Test
    fun `Removing armies subtracts from the amount of armies`() {
        val originalArmies = 3
        val occupation = SinglePlayerOccupation(somePlayer, originalArmies)

        val removed = 1
        occupation.removeArmies(removed)

        assertEquals(originalArmies - removed, occupation.armies)
    }

    @Test
    fun `Can't remove a negative amount of armies`() {
        val originalArmies = 2
        val occupation = SinglePlayerOccupation(somePlayer, originalArmies)

        val removed = -3
        val exception = assertFailsWith<NonPositiveArmiesRemovedException> {
            occupation.removeArmies(removed)
        }

        assertEquals(removed, exception.armies)
        assertEquals(originalArmies, occupation.armies)
    }

    @Test
    fun `Can't remove zero armies`() {
        val originalArmies = 2
        val occupation = SinglePlayerOccupation(somePlayer, originalArmies)

        val removed = 0
        val exception = assertFailsWith<NonPositiveArmiesRemovedException> {
            occupation.removeArmies(removed)
        }

        assertEquals(removed, exception.armies)
        assertEquals(originalArmies, occupation.armies)
    }

    @Test
    fun `Can't remove more armies than there are currently in the occupation`() {
        val originalArmies = 2
        val occupation = SinglePlayerOccupation(somePlayer, originalArmies)

        val removed = 3
        val exception = assertFailsWith<TooManyArmiesRemovedException> {
            occupation.removeArmies(removed)
        }

        assertEquals(originalArmies, exception.currentArmies)
        assertEquals(removed, exception.removed)
        assertEquals(originalArmies, occupation.armies)
    }

    @Test
    fun `Can't remove armies and leave the occupation with zero armies`() {
        val originalArmies = 2
        val occupation = SinglePlayerOccupation(somePlayer, originalArmies)

        val exception = assertFailsWith<TooManyArmiesRemovedException> {
            occupation.removeArmies(originalArmies)
        }

        assertEquals(originalArmies, exception.currentArmies)
        assertEquals(originalArmies, exception.removed)
        assertEquals(originalArmies, occupation.armies)
    }
}

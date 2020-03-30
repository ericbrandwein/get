package armies

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class SharedOccupationTest {

    private val somePlayer = "Olivia"
    private val otherPlayer = "Javiera"
    private val anotherPlayer = "Alejandra"
    private val evenOtherPlayer = "Chewbacca"

    @Test
    fun `SharedOccupation means it's occupied`() {
        val occupation = SharedOccupation(somePlayer, otherPlayer, 1, 2)
        assertTrue(occupation.isOccupied())
    }

    @Test
    fun `SharedOccupation is shared`() {
        val occupation = SharedOccupation(somePlayer, otherPlayer, 1, 2)
        assertTrue(occupation.isShared())
    }

    @Test
    fun `Occupiers of a SharedOccupation are the ones passed as a constructor parameter`() {
        val occupation = SharedOccupation(somePlayer, otherPlayer, 1, 2)

        val occupiers = occupation.occupiers
        assertEquals(2, occupiers.size)
        assertTrue(somePlayer in occupiers)
        assertTrue(otherPlayer in occupiers)
    }

    @Test
    fun `Both occupiers can't be the same player`() {
        val exception = assertFailsWith<CantShareCountryWithYourselfException> {
            SharedOccupation(somePlayer, somePlayer, 1, 2)
        }
        assertEquals(somePlayer, exception.player)
    }

    @Test
    fun `Can't occupy with a negative amount of armies for the first player`() {
        val armies = -3

        val exception = assertFailsWith<NonPositiveArmiesException> {
            SharedOccupation(somePlayer, otherPlayer, armies, 3)
        }
        assertEquals(armies, exception.armies)
    }

    @Test
    fun `Can't occupy with zero armies for the first player`() {
        val armies = 0

        val exception = assertFailsWith<NonPositiveArmiesException> {
            SharedOccupation(somePlayer, otherPlayer, armies, 3)
        }
        assertEquals(armies, exception.armies)
    }

    @Test
    fun `Can't occupy with a non-positive amount of armies for the second player`() {
        val armies = -2

        val exception = assertFailsWith<NonPositiveArmiesException> {
            SharedOccupation(somePlayer, otherPlayer, 2, armies)
        }
        assertEquals(armies, exception.armies)
    }

    @Test
    fun `Armies of the first occupier are the armies passed first as a constructor parameter`() {
        val firstArmies = 1
        val occupation = SharedOccupation(somePlayer, otherPlayer, firstArmies, 2)

        assertEquals(firstArmies, occupation.armiesOf(somePlayer))
    }

    @Test
    fun `Armies of the second occupier are the armies passed second as a constructor parameter`() {
        val secondArmies = 2
        val occupation = SharedOccupation(somePlayer, otherPlayer, 1, secondArmies)

        assertEquals(secondArmies, occupation.armiesOf(otherPlayer))
    }

    @Test
    fun `Can't ask armies of a non-occupying player`() {
        val occupation = SharedOccupation(somePlayer, otherPlayer, 1, 2)

        val exception = assertFailsWith<NotOccupyingPlayerException> {
            occupation.armiesOf(anotherPlayer)
        }
        assertEquals(anotherPlayer, exception.player)
    }

    @Test
    fun `Occupying with one player returns an occupation with only that player`() {
        val occupation = SharedOccupation(somePlayer, otherPlayer, 1, 2)

        val armies = 3
        val newOccupation = occupation.occupy(anotherPlayer, armies)

        assertEquals(anotherPlayer, newOccupation.occupier)
        assertEquals(armies, newOccupation.armies)
    }

    @Test
    fun `Can't occupy with player already occupying country`() {
        val occupation = SharedOccupation(somePlayer, otherPlayer, 1, 2)

        val exception = assertFailsWith<PlayerAlreadyOccupiesCountryException> {
            occupation.occupy(somePlayer, 3)
        }

        assertEquals(somePlayer, exception.player)
    }

    @Test
    fun `Occupying with two new players a country should return an occupation with both players`() {
        val occupation = SharedOccupation(somePlayer, otherPlayer, 1, 2)

        val firstArmies = 4
        val secondArmies = 7
        val newOccupation =
            occupation.occupy(anotherPlayer, evenOtherPlayer, firstArmies, secondArmies)

        assertEquals(setOf(anotherPlayer, evenOtherPlayer), newOccupation.occupiers)
        assertEquals(firstArmies, newOccupation.armiesOf(anotherPlayer))
        assertEquals(secondArmies, newOccupation.armiesOf(evenOtherPlayer))
    }

    @Test
    fun `Can't occupy a SharedOccupation when the first player is already occupying the country`() {
        val occupation = SharedOccupation(somePlayer, otherPlayer, 1, 2)

        val exception = assertFailsWith<PlayerAlreadyOccupiesCountryException> {
            occupation.occupy(somePlayer, anotherPlayer, 3, 5)
        }
        assertEquals(somePlayer, exception.player)
    }

    @Test
    fun `Can't occupy a SharedOccupation when the second player is already occupying the country`() {
        val occupation = SharedOccupation(somePlayer, otherPlayer, 1, 2)

        val exception = assertFailsWith<PlayerAlreadyOccupiesCountryException> {
            occupation.occupy(anotherPlayer, somePlayer, 3, 5)
        }
        assertEquals(somePlayer, exception.player)
    }

}
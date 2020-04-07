package countries

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals

class OccupationTest {

    private fun occupationWithArmies(armies: Int) =
        Occupation("Argentina", "Eric", armies)

    @Test
    fun `Creating an occupation set the required members`() {
        val country = "Argentina"
        val player = "Juan"
        val armies = 1
        val occupation = Occupation(country, player, armies)

        assertEquals(country, occupation.country)
        assertEquals(player, occupation.occupier)
        assertEquals(armies, occupation.armies)
    }

    @Test
    fun `Can't create an Occupation with a non-positive amount of armies`() {
        val armies = 0
        val exception = assertFailsWith<NonPositiveArmiesException> {
            occupationWithArmies(armies)
        }
        assertEquals(armies, exception.armies)
    }

    @Test
    fun `Adding armies to an Occupation increases the amount of armies it has`() {
        val armies = 1
        val occupation = occupationWithArmies(armies)

        val added = 2
        occupation.addArmies(added)

        assertEquals(armies + added, occupation.armies)
    }

    @Test
    fun `Can't add a negative amount of armies to an Occupation`() {
        val armies = 1
        val occupation = occupationWithArmies(armies)

        val added = -1
        val exception = assertFailsWith<NonPositiveArmiesException> {
            occupation.addArmies(added)
        }

        assertEquals(added, exception.armies)
        assertEquals(armies, occupation.armies)
    }

    @Test
    fun `Can't add zero armies to an Occupation`() {
        val armies = 1
        val occupation = occupationWithArmies(armies)

        val added = 0
        val exception = assertFailsWith<NonPositiveArmiesException> {
            occupation.addArmies(added)
        }

        assertEquals(added, exception.armies)
        assertEquals(armies, occupation.armies)
    }

    @Test
    fun `Removing armies subtracts from the amount of armies`() {
        val armies = 3
        val occupation = occupationWithArmies(armies)

        val removed = 2
        occupation.removeArmies(removed)

        assertEquals(armies - removed, occupation.armies)
    }

    @Test
    fun `Can't remove a non-positive amount of armies`() {
        val armies = 3
        val occupation = occupationWithArmies(armies)

        val removed = 0
        val exception = assertFailsWith<NonPositiveArmiesException> {
            occupation.removeArmies(removed)
        }

        assertEquals(removed, exception.armies)
    }

    @Test
    fun `Can't leave the Occupation with less than one army`() {
        val armies = 3
        val occupation = occupationWithArmies(armies)

        val removed = 3
        val exception = assertFailsWith<TooManyArmiesRemovedException> {
            occupation.removeArmies(removed)
        }

        assertEquals(removed, exception.armies)
    }

    @Test
    fun `Occupation equals another Occupation with same members`() {
        val firstOccupation = occupationWithArmies(1)
        val secondOccupation = occupationWithArmies(1)

        assertEquals(firstOccupation, secondOccupation)
    }

    @Test
    fun `Occupation does not equal an object that is not an Occupation`() {
        val otherObject = Object()
        val occupation = occupationWithArmies(1)

        assertNotEquals<Any>(occupation, otherObject)
    }

    @Test
    fun `Occupation does not equal another Occupation with different country`() {
        val firstOccupation = Occupation("Argentina", "Eric", 1)
        val secondOccupation = Occupation("Uruguay", "Eric", 1)

        assertNotEquals(firstOccupation, secondOccupation)
    }

    @Test
    fun `Occupation does not equal another Occupation with different occupier`() {
        val firstOccupation = Occupation("Argentina", "Eric", 1)
        val secondOccupation = Occupation("Argentina", "Nico", 1)

        assertNotEquals(firstOccupation, secondOccupation)
    }

    @Test
    fun `Occupation does not equal another Occupation with different armies`() {
        val firstOccupation = Occupation("Argentina", "Eric", 1)
        val secondOccupation = Occupation("Argentina", "Eric", 2)

        assertNotEquals(firstOccupation, secondOccupation)
    }
}

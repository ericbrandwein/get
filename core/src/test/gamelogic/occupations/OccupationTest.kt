package gamelogic.occupations

import PositiveInt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals

class OccupationTest {

    private fun occupationWithArmies(armies: PositiveInt) =
        Occupation("Argentina", "Eric", armies)

    @Test
    fun `Creating an occupation set the required members`() {
        val country = "Argentina"
        val player = "Juan"
        val armies = PositiveInt(1)
        val occupation = Occupation(country, player, armies)

        assertEquals(country, occupation.country)
        assertEquals(player, occupation.occupier)
        assertEquals(armies, occupation.armies)
    }

    @Test
    fun `Adding armies to an Occupation increases the amount of armies it has`() {
        val armies = PositiveInt(1)
        val occupation = occupationWithArmies(armies)

        val added = PositiveInt(2)
        occupation.addArmies(added)

        assertEquals(armies + added, occupation.armies)
    }

    @Test
    fun `Removing armies subtracts from the amount of armies`() {
        val armies = PositiveInt(3)
        val occupation = occupationWithArmies(armies)

        val removed = PositiveInt(2)
        occupation.removeArmies(removed)

        assertEquals(armies - removed, occupation.armies)
    }

    @Test
    fun `Can't leave the Occupation with less than one army`() {
        val armies = PositiveInt(3)
        val occupation = occupationWithArmies(armies)

        val removed = PositiveInt(3)
        val exception = assertFailsWith<TooManyArmiesRemovedException> {
            occupation.removeArmies(removed)
        }

        assertEquals(removed, exception.armies)
    }

    @Test
    fun `Occupation equals another Occupation with same members`() {
        val firstOccupation = occupationWithArmies(PositiveInt(1))
        val secondOccupation = occupationWithArmies(PositiveInt(1))

        assertEquals(firstOccupation, secondOccupation)
    }

    @Test
    fun `Occupation does not equal an object that is not an Occupation`() {
        val otherObject = Object()
        val occupation = occupationWithArmies(PositiveInt(1))

        assertNotEquals<Any>(occupation, otherObject)
    }

    @Test
    fun `Occupation does not equal another Occupation with different country`() {
        val firstOccupation = Occupation("Argentina", "Eric", PositiveInt(1))
        val secondOccupation = Occupation("Uruguay", "Eric", PositiveInt(1))

        assertNotEquals(firstOccupation, secondOccupation)
    }

    @Test
    fun `Occupation does not equal another Occupation with different occupier`() {
        val firstOccupation = Occupation("Argentina", "Eric", PositiveInt(1))
        val secondOccupation = Occupation("Argentina", "Nico", PositiveInt(1))

        assertNotEquals(firstOccupation, secondOccupation)
    }

    @Test
    fun `Occupation does not equal another Occupation with different armies`() {
        val firstOccupation = Occupation("Argentina", "Eric", PositiveInt(1))
        val secondOccupation = Occupation("Argentina", "Eric", PositiveInt(2))

        assertNotEquals(firstOccupation, secondOccupation)
    }
}

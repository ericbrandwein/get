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
            occupation.occupy(otherPlayer, anotherPlayer, firstArmies, secondArmies)

        assertEquals(setOf(otherPlayer, anotherPlayer), newOccupation.occupiers)
        assertEquals(firstArmies, newOccupation.armiesOf(otherPlayer))
        assertEquals(secondArmies, newOccupation.armiesOf(anotherPlayer))
    }

    @Test
    fun `Can't occupy with two players if the first is the same one currently occupying`() {
        val occupation = SinglePlayerOccupation(somePlayer, 3)

        val exception = assertFailsWith<PlayerAlreadyOccupiesCountryException> {
            occupation.occupy(somePlayer, otherPlayer, 1, 2)
        }
        assertEquals(somePlayer, exception.player)
    }

    @Test
    fun `Can't occupy with two players if the second is the same one currently occupying`() {
        val occupation = SinglePlayerOccupation(somePlayer, 3)

        val exception = assertFailsWith<PlayerAlreadyOccupiesCountryException> {
            occupation.occupy(otherPlayer, somePlayer, 1, 2)
        }
        assertEquals(somePlayer, exception.player)
    }


}

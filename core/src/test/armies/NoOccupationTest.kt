package armies

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class NoOccupationTest {

    private lateinit var occupation: NoOccupation
    private val somePlayer = "Pepito"
    private val otherPlayer = "Elsa"
    private val someArmies = 3
    private val otherArmies = 5

    @BeforeTest
    fun setup() {
        occupation = NoOccupation()
    }

    @Test
    fun `NoOccupation means the country is not occupied`() {
        assertFalse(occupation.isOccupied())
    }

    @Test
    fun `NoOccupation is not shared`() {
        assertFalse(occupation.isShared())
    }

    @Test
    fun `Occupying a NoOccupation with only one player returns an occupation with that player and armies`() {
        val newOccupation = occupation.occupy(somePlayer, someArmies)

        assertEquals(somePlayer, newOccupation.occupier)
        assertEquals(someArmies, newOccupation.armies)
    }

    @Test
    fun `Occupying a NoOccupation with two players returns an occupation with that players and armies`() {
        val newOccupation =
            occupation.occupy(somePlayer, someArmies, otherPlayer, otherArmies)

        assertEquals(setOf(somePlayer, otherPlayer), newOccupation.occupiers)
        assertEquals(someArmies, newOccupation.armiesOf(somePlayer))
        assertEquals(otherArmies, newOccupation.armiesOf(otherPlayer))
    }
}

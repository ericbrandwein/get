package occupations

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
}

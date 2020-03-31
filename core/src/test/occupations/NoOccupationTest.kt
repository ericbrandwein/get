package occupations

import kotlin.test.*

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
    fun `visitNoOccupation gets called when visiting a NoOccupation`() {
        var timesCalled = 0
        val visitor = object : OccupationVisitor {
            override fun visit(occupation: NoOccupation) {
                timesCalled++
            }

            override fun visit(occupation: SinglePlayerOccupation) {
                fail("Should not visit a SinglePlayerOccupation")
            }

            override fun visit(occupation: SharedOccupation) {
                fail("Should not visit a SharedOccupation")
            }
        }

        occupation.accept(visitor)

        assertEquals(1, timesCalled, "visit(NoOccupation) should be called one time.")
    }
}

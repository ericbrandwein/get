package occupations

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

    @Test
    fun `Correct visit gets called when visiting a SinglePlayerOccupation`() {
        var timesCalled = 0
        val visitor = object : OccupationVisitor {
            override fun visit(occupation: NoOccupation) {
                fail("Should not visit a NoOccupation")
            }

            override fun visit(occupation: SinglePlayerOccupation) {
                timesCalled++
            }

            override fun visit(occupation: SharedOccupation) {
                fail("Should not visit a SharedOccupation")
            }
        }

        val occupation = SinglePlayerOccupation(somePlayer, 1)
        occupation.accept(visitor)

        assertEquals(
            1, timesCalled, "visit(SinglePlayerOccupation) should be called one time.")
    }
}

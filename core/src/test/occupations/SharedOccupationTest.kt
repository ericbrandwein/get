package occupations

import kotlin.test.*

class SharedOccupationTest {

    private val somePlayer = "Olivia"
    private val otherPlayer = "Javiera"
    private val anotherPlayer = "Alejandra"
    private val evenOtherPlayer = "Chewbacca"

    @Test
    fun `SharedOccupation means it's occupied`() {
        val occupation = SharedOccupation(somePlayer, 1, otherPlayer, 2)
        assertTrue(occupation.isOccupied())
    }

    @Test
    fun `SharedOccupation is shared`() {
        val occupation = SharedOccupation(somePlayer, 1, otherPlayer, 2)
        assertTrue(occupation.isShared())
    }

    @Test
    fun `Occupiers of a SharedOccupation are the ones passed as a constructor parameter`() {
        val occupation = SharedOccupation(somePlayer, 1, otherPlayer, 2)

        val occupiers = occupation.occupiers
        assertEquals(2, occupiers.size)
        assertTrue(somePlayer in occupiers)
        assertTrue(otherPlayer in occupiers)
    }

    @Test
    fun `Both occupiers can't be the same player`() {
        val exception = assertFailsWith<CantShareCountryWithYourselfException> {
            SharedOccupation(somePlayer, 1, somePlayer, 2)
        }
        assertEquals(somePlayer, exception.player)
    }

    @Test
    fun `Can't occupy with a negative amount of armies for the first player`() {
        val armies = -3

        val exception = assertFailsWith<NonPositiveArmiesException> {
            SharedOccupation(somePlayer, armies, otherPlayer, 3)
        }
        assertEquals(armies, exception.armies)
    }

    @Test
    fun `Can't occupy with zero armies for the first player`() {
        val armies = 0

        val exception = assertFailsWith<NonPositiveArmiesException> {
            SharedOccupation(somePlayer, armies, otherPlayer, 3)
        }
        assertEquals(armies, exception.armies)
    }

    @Test
    fun `Can't occupy with a non-positive amount of armies for the second player`() {
        val armies = -2

        val exception = assertFailsWith<NonPositiveArmiesException> {
            SharedOccupation(somePlayer, 2, otherPlayer, armies)
        }
        assertEquals(armies, exception.armies)
    }

    @Test
    fun `Armies of the first occupier are the armies passed first as a constructor parameter`() {
        val firstArmies = 1
        val occupation = SharedOccupation(somePlayer, firstArmies, otherPlayer, 2)

        assertEquals(firstArmies, occupation.armiesOf(somePlayer))
    }

    @Test
    fun `Armies of the second occupier are the armies passed second as a constructor parameter`() {
        val secondArmies = 2
        val occupation = SharedOccupation(somePlayer, 1, otherPlayer, secondArmies)

        assertEquals(secondArmies, occupation.armiesOf(otherPlayer))
    }

    @Test
    fun `Can't ask armies of a non-occupying player`() {
        val occupation = SharedOccupation(somePlayer, 1, otherPlayer, 2)

        val exception = assertFailsWith<NotOccupyingPlayerException> {
            occupation.armiesOf(anotherPlayer)
        }
        assertEquals(anotherPlayer, exception.player)
    }

    @Test
    fun `Adding armies to a player changes the amount of armies of that player only`() {
        val firstArmies = 2
        val secondArmies = 1
        val occupation = SharedOccupation(
            somePlayer, firstArmies, otherPlayer, secondArmies)

        val added = 5
        occupation.addArmies(added, somePlayer)

        assertEquals(firstArmies + added, occupation.armiesOf(somePlayer))
        assertEquals(secondArmies, occupation.armiesOf(otherPlayer))
    }

    @Test
    fun `Can't add a negative amount of armies to a player`() {
        val firstArmies = 4
        val secondArmies = 1
        val occupation = SharedOccupation(
            somePlayer, firstArmies, otherPlayer, secondArmies)

        val added = -2
        val exception = assertFailsWith<NonPositiveArmiesAddedException> {
            occupation.addArmies(added, somePlayer)
        }

        assertEquals(added, exception.armies)
    }

    @Test
    fun `Can't add zero armies to a player`() {
        val firstArmies = 4
        val secondArmies = 1
        val occupation = SharedOccupation(
            somePlayer, firstArmies, otherPlayer, secondArmies)

        val added = 0
        val exception = assertFailsWith<NonPositiveArmiesAddedException> {
            occupation.addArmies(added, somePlayer)
        }

        assertEquals(added, exception.armies)
    }

    @Test
    fun `Removing armies from a player subtracts from the amount of armies`() {
        val original = 4
        val occupation =
            SharedOccupation(somePlayer, original, otherPlayer, 1)

        val removed = 3
        occupation.removeArmies(removed, somePlayer)

        assertEquals(original - removed, occupation.armiesOf(somePlayer))
    }

    @Test
    fun `Can't remove a non-positive amount of armies from a player`() {
        val original = 4
        val occupation =
            SharedOccupation(somePlayer, original, otherPlayer, 1)

        val removed = -2
        val exception = assertFailsWith<NonPositiveArmiesRemovedException> {
            occupation.removeArmies(removed, somePlayer)
        }
        assertEquals(removed, exception.armies)
        assertEquals(original, occupation.armiesOf(somePlayer))
    }

    @Test
    fun `Occupying a player's share changes the removed player for the occupier`() {
        val firstArmies = 2
        val occupation =
            SharedOccupation(somePlayer, 1, otherPlayer, firstArmies)

        val secondArmies = 3
        occupation.replacePlayer(somePlayer, anotherPlayer, secondArmies)

        assertEquals(setOf(otherPlayer, anotherPlayer), occupation.occupiers)
        assertEquals(firstArmies, occupation.armiesOf(otherPlayer))
        assertEquals(secondArmies, occupation.armiesOf(anotherPlayer))
    }

    @Test
    fun `Replaced player has to be an occupier`() {
        val occupation = SharedOccupation(somePlayer, 1, otherPlayer, 2)

        val exception = assertFailsWith<NotOccupyingPlayerException> {
            occupation.replacePlayer(anotherPlayer, evenOtherPlayer, 2)
        }
        assertEquals(anotherPlayer, exception.player)
        assertEquals(setOf(somePlayer, otherPlayer), occupation.occupiers)
    }

    @Test
    fun `New replacing player can't be already occupying`() {
        val armies = 1
        val occupation = SharedOccupation(somePlayer, armies, otherPlayer, 2)

        val exception = assertFailsWith<PlayerAlreadyOccupiesCountryException> {
            occupation.replacePlayer(somePlayer, otherPlayer, 3)
        }
        assertEquals(otherPlayer, exception.player)
        assertEquals(setOf(somePlayer, otherPlayer), occupation.occupiers)
        assertEquals(armies, occupation.armiesOf(somePlayer))
    }

    @Test
    fun `Correct visit gets called when visiting a SharedOccupation`() {
        var timesCalled = 0
        val visitor = object : OccupationVisitor {
            override fun visit(occupation: NoOccupation) {
                fail("Should not visit a NoOccupation.")
            }

            override fun visit(occupation: SinglePlayerOccupation) {
                fail("Should not visit a SinglePlayerOccupation.")
            }

            override fun visit(occupation: SharedOccupation) {
                timesCalled++
            }
        }

        val occupation = SharedOccupation(somePlayer, 1, otherPlayer, 2)
        occupation.accept(visitor)

        assertEquals(1, timesCalled, "visit(SharedOccupation) should be called one time")
    }
}

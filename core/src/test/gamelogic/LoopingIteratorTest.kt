package gamelogic

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class LoopingIteratorTest {
    @Test
    fun `Can't create a LoopingIterator from an empty List`() {
        val list = mutableListOf<Int>()

        assertFailsWith<UnsupportedOperationException> {
            list.loopingIterator()
        }
    }

    @Test
    fun `get() of a LoopingIterator on a List returns the first element`() {
        val element = 1
        val list = mutableListOf(element)

        val iterator = list.loopingIterator()

        assertEquals(element, iterator.current)
    }

    @Test
    fun `next() returns the second element if there is one`() {
        val secondElement = 2
        val list = mutableListOf(1, secondElement)

        val iterator = list.loopingIterator()

        assertEquals(secondElement, iterator.next())
    }

    @Test
    fun `get() returns the element returned last by next()`() {
        val secondElement = 2
        val list = mutableListOf(1, secondElement)

        val iterator = list.loopingIterator()
        iterator.next()

        assertEquals(secondElement, iterator.current)
    }

    @Test
    fun `next() returns first element if there's only one element`() {
        val element = "hola"
        val list = mutableListOf(element)

        val iterator = list.loopingIterator()

        assertEquals(element, iterator.next())
    }

    @Test
    fun `Can't remove last element`() {
        val list = mutableListOf("element")

        val iterator =  list.loopingIterator()

        assertFailsWith<UnsupportedOperationException> {
            iterator.remove()
        }
    }

    @Test
    fun `Removing the first element removes it from the list and sets the current player to the next`() {
        val secondElement = "element"
        val list = mutableListOf("first", secondElement)

        val iterator = list.loopingIterator()
        iterator.remove()

        assertEquals(mutableListOf(secondElement), list)
        assertEquals(secondElement, iterator.current)
    }

    @Test
    fun `Removing the last element moves the current to the first element`() {
        val firstElement = "fisrt"
        val list = mutableListOf(firstElement, "second")

        val iterator = list.loopingIterator()
        iterator.next()
        iterator.remove()

        assertEquals(mutableListOf(firstElement), list)
        assertEquals(firstElement, iterator.current)
    }

    @Test
    fun `currentIndex returns 0 when creating the LoopingIterator`() {
        val iterator = mutableListOf(1).loopingIterator()

        assertEquals(0, iterator.currentIndex)
    }

    @Test
    fun `currentIndex returns the appropiate index when calling next()`() {
        val iterator = mutableListOf(1, 2).loopingIterator()
        iterator.next()

        assertEquals(1, iterator.currentIndex)
    }

    @Test
    fun `Can't call removeAt() with an index outside the List`() {
        val list = mutableListOf(1, 2)

        assertFailsWith<IndexOutOfBoundsException> {
            list.loopingIterator().removeAt(2)
        }
    }

    @Test
    fun `Can't call removeAt() when there's only one element remaining`() {
        val list = mutableListOf(1)
        val iterator = list.loopingIterator()

        assertFailsWith<UnsupportedOperationException> {
            iterator.removeAt(0)
        }
        assertEquals(mutableListOf(1), list)
    }

    @Test
    fun `removeAt() advances current if removing at same index as current`() {
        val second = 2
        val list = mutableListOf(1, second)

        val iterator = list.loopingIterator()
        iterator.removeAt(0)

        assertEquals(mutableListOf(second), list)
        assertEquals(second, iterator.current)
        assertEquals(0, iterator.currentIndex)
    }

    @Test
    fun `removeAt() does not advance current if removing after current`() {
        val first = 1
        val list = mutableListOf(first, 2)

        val iterator = list.loopingIterator()
        iterator.removeAt(1)

        assertEquals(mutableListOf(first), list)
        assertEquals(first, iterator.current)
        assertEquals(0, iterator.currentIndex)

    }

    @Test
    fun `removeAt() advances current if removing at same index as current with more than 2 elements`() {
        val second = 2
        val third = 3
        val list = mutableListOf(1, second, third)

        val iterator = list.loopingIterator()
        iterator.removeAt(0)

        assertEquals(mutableListOf(second, third), list)
        assertEquals(second, iterator.current)
        assertEquals(0, iterator.currentIndex)
    }

    @Test
    fun `removeAt() decrements index if removing before current`() {
        val list = mutableListOf(1, 2, 3)

        val iterator = list.loopingIterator()
        iterator.next()
        iterator.removeAt(0)

        assertEquals(mutableListOf(2, 3), list)
        assertEquals(2, iterator.current)
        assertEquals(0, iterator.currentIndex)
    }

    @Test
    fun `nextIndex() returns currentIndex + 1 if it is inside the list`() {
        val iterator = mutableListOf(1, 2).loopingIterator()

        assertEquals(1, iterator.nextIndex())
    }

    @Test
    fun `nextIndex() returns 0 if at last element`() {
        val iterator = mutableListOf(1).loopingIterator()

        assertEquals(0, iterator.nextIndex())
    }

    @Test
    fun `hasNext() always returns true`() {
        assertTrue(mutableListOf(1).loopingIterator().hasNext())
    }

    @Test
    fun `hasPrevious() always returns true`() {
        assertTrue(mutableListOf(1).loopingIterator().hasPrevious())
    }

    @Test
    fun `previousIndex returns currentIndex - 1 if inside the list`() {
        val iterator = mutableListOf(1, 2).loopingIterator()
        iterator.next()

        assertEquals(0, iterator.previousIndex())
    }

    @Test
    fun `previousIndex() returns lastIndex if currentIndex is 0`() {
        val iterator = mutableListOf(1, 2).loopingIterator()

        assertEquals(1, iterator.previousIndex())
    }

    @Test
    fun `previous() moves to the previousIndex element`() {
        val list = mutableListOf(1, 2)

        val iterator = list.loopingIterator()

        assertEquals(2, iterator.previous())
        assertEquals(1, iterator.currentIndex)
    }
}


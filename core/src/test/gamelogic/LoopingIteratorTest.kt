package gamelogic

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class LoopingIteratorTest {
    @Test
    fun `Can't create a LoopingIterator from an empty list`() {
        val list = emptyList<Int>()

        assertFailsWith<UnsupportedOperationException> {
            list.loopingIterator()
        }
    }

    @Test
    fun `get() of a LoopingIterator on a Collection returns the first element`() {
        val element = 1
        val collection = listOf(element)

        val iterator = collection.loopingIterator()

        assertEquals(element, iterator.get())
    }

    @Test
    fun `next() returns the second element if there is one`() {
        val secondElement = 2
        val collection = listOf(1, secondElement)

        val iterator = collection.loopingIterator()

        assertEquals(secondElement, iterator.next())
    }

    @Test
    fun `hasNext() always returns true`() {
        val collection = listOf(1)

        val iterator = collection.loopingIterator()

        assertTrue(iterator.hasNext())
    }

    @Test
    fun `get() returns the element returned last by next()`() {
        val secondElement = 2
        val collection = listOf(1, secondElement)

        val iterator = collection.loopingIterator()
        iterator.next()

        assertEquals(secondElement, iterator.get())
    }

    @Test
    fun `next() returns first element if there's only one element`() {
        val element = "hola"
        val collection = listOf(element)

        val iterator = collection.loopingIterator()

        assertEquals(element, iterator.next())
    }
}


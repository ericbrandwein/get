import kotlin.test.*

class PositiveIntTest {

    @Test
    fun `Can't create from a negative Int`() {
        val negative = -3

        val exception = assertFailsWith<NonPositiveNumberException> {
            PositiveInt.fromInt(negative)
        }

        assertEquals(negative, exception.number)
    }
    @Test
    fun `Can't create from Int zero`() {
        val zero = 0

        val exception = assertFailsWith<NonPositiveNumberException> {
            PositiveInt.fromInt(zero)
        }

        assertEquals(zero, exception.number)
    }

    @Test
    fun `Two PositiveInts created with same Int are equal`() {
        val first = PositiveInt.fromInt(23)
        val second = PositiveInt.fromInt(23)
        assertEquals(first, second)
    }

    @Test
    fun `Two PositiveInts created with different Ints are different`() {
        val first = PositiveInt.fromInt(322)
        val second = PositiveInt.fromInt(1)
        assertNotEquals(first, second)
    }

    @Test
    fun `A PositiveInt is not equal to objects of other classes`() {
        val first = PositiveInt.fromInt(32)
        val second = Object()

        assertNotEquals<Any>(first, second)
    }

    @Test
    fun `toInt returns the Int with which it was created`() {
        val integer = 2

        val positiveInteger = PositiveInt.fromInt(integer)

        assertEquals(integer, positiveInteger.toInt())
    }

    @Test
    fun `toByte returns the toByte of the Int with which it was created`() {
        val integer = 3

        val positiveInteger = PositiveInt.fromInt(integer)

        assertEquals(integer.toByte(), positiveInteger.toByte())
    }

    @Test
    fun `toChar returns the toChar of the Int with which it was created`() {
        val integer = 123

        val positiveInteger = PositiveInt.fromInt(integer)

        assertEquals(integer.toChar(), positiveInteger.toChar())
    }

    @Test
    fun `toDouble returns the toDouble of the Int with which it was created`() {
        val integer = 432

        val positiveInteger = PositiveInt.fromInt(integer)

        assertEquals(integer.toDouble(), positiveInteger.toDouble())
    }

    @Test
    fun `toFloat returns the toFloat of the Int with which it was created`() {
        val integer = 654

        val positiveInteger = PositiveInt.fromInt(integer)

        assertEquals(integer.toFloat(), positiveInteger.toFloat())
    }

    @Test
    fun `toLong returns the toLong of the Int with which it was created`() {
        val integer = 98765

        val positiveInteger = PositiveInt.fromInt(integer)

        assertEquals(integer.toLong(), positiveInteger.toLong())
    }

    @Test
    fun `toShort returns the toShort of the Int with which it was created`() {
        val integer = 13123

        val positiveInteger = PositiveInt.fromInt(integer)

        assertEquals(integer.toShort(), positiveInteger.toShort())
    }

    @Test
    fun `compareTo returns the compareTo of the Int with which it was created`() {
        val firstInt = 23
        val secondInt = 12

        val firstPositive = PositiveInt.fromInt(firstInt)
        val secondPositive = PositiveInt.fromInt(secondInt)

        assertTrue(firstPositive > secondPositive)
    }

    @Test
    fun `Adding a PositiveInt with another PositiveInt returns the sum of them`() {
        val first = PositiveInt.fromInt(23)
        val second = PositiveInt.fromInt(7876)

        val expected = first.toInt() + second.toInt()
        assertEquals(expected, (first + second).toInt())
    }
    
    @Test
    fun `Subtracting a PositiveInt from another PositiveInt returns the difference`() {
        val first = PositiveInt.fromInt(34)
        val second = PositiveInt.fromInt(1)

        val expected = first.toInt() - second.toInt()
        assertEquals(expected, (first - second).toInt())
    }

    @Test
    fun `Can't subtract a bigger or equal PositiveInt from a PositiveInt`() {
        val first = PositiveInt.fromInt(42)
        val second = PositiveInt.fromInt(98934)

        val exception = assertFailsWith<TooBigToSubtractException> {
            first - second
        }

        assertEquals(first, exception.minuend)
        assertEquals(second, exception.subtrahend)
    }

    @Test
    fun `times() multiplies the two PositiveInts`() {
        val first = PositiveInt.fromInt(2)
        val second = PositiveInt.fromInt(5)

        val expected = PositiveInt.fromInt(10)
        assertEquals(expected,first * second)
    }

    @Test
    fun `div() divides the two PositiveInts`() {
        val first = PositiveInt.fromInt(11)
        val second = PositiveInt.fromInt(2)

        val expected = PositiveInt.fromInt(5)
        assertEquals(expected, first / second)
    }

    @Test
    fun `rem() calculates the remainder between the two PositiveInts`() {
        val first = PositiveInt.fromInt(4)
        val second = PositiveInt.fromInt(3)

        val expected = PositiveInt.fromInt(1)
        assertEquals(expected, first % second)
    }

    @Test
    fun `inc() increments the PositiveInt`() {
        var number = PositiveInt.fromInt(23)

        number++

        val expected = PositiveInt.fromInt(24)
        assertEquals(expected, number)
    }

    @Test
    fun `dec() decrements the PositiveInt`() {
        var number = PositiveInt.fromInt(43)

        number--

        val expected = PositiveInt.fromInt(42)
        assertEquals(expected, number)
    }

    @Test
    fun `Can't decrement PositiveInt from Int 1`() {
        var number = PositiveInt.fromInt(1)

        assertFailsWith<CantDecrementException> {
            number--
        }
    }

    @Test
    fun `rangeTo() creates a range that corresponds to the same ComparableRange`() {
        val first = PositiveInt.fromInt(1)
        val second = PositiveInt.fromInt(5)

        val range = first..second

        assertTrue(first in range)
        assertTrue(second in range)
        assertTrue(PositiveInt.fromInt(3) in range)
    }
}

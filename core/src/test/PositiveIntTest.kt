import kotlin.test.*

class PositiveIntTest {

    @Test
    fun `Can't create from a negative Int`() {
        val negative = -3

        val exception = assertFailsWith<NonPositiveNumberException> {
            PositiveInt(negative)
        }

        assertEquals(negative, exception.number)
    }

    @Test
    fun `Can't create from Int zero`() {
        val zero = 0

        val exception = assertFailsWith<NonPositiveNumberException> {
            PositiveInt(zero)
        }

        assertEquals(zero, exception.number)
    }

    @Test
    fun `Two PositiveInts created with same Int are equal`() {
        val first = PositiveInt(23)
        val second = PositiveInt(23)
        assertEquals(first, second)
    }

    @Test
    fun `Two PositiveInts created with different Ints are different`() {
        val first = PositiveInt(322)
        val second = PositiveInt(1)
        assertNotEquals(first, second)
    }

    @Test
    fun `A PositiveInt is not equal to objects of other classes`() {
        val first = PositiveInt(32)
        val second = Object()

        assertNotEquals<Any>(first, second)
    }

    @Test
    fun `toInt returns the Int with which it was created`() {
        val integer = 2

        val positiveInteger = PositiveInt(integer)

        assertEquals(integer, positiveInteger.toInt())
    }

    @Test
    fun `toByte returns the toByte of the Int with which it was created`() {
        val integer = 3

        val positiveInteger = PositiveInt(integer)

        assertEquals(integer.toByte(), positiveInteger.toByte())
    }

    @Test
    fun `toChar returns the toChar of the Int with which it was created`() {
        val integer = 123

        val positiveInteger = PositiveInt(integer)

        assertEquals(integer.toChar(), positiveInteger.toChar())
    }

    @Test
    fun `toDouble returns the toDouble of the Int with which it was created`() {
        val integer = 432

        val positiveInteger = PositiveInt(integer)

        assertEquals(integer.toDouble(), positiveInteger.toDouble())
    }

    @Test
    fun `toFloat returns the toFloat of the Int with which it was created`() {
        val integer = 654

        val positiveInteger = PositiveInt(integer)

        assertEquals(integer.toFloat(), positiveInteger.toFloat())
    }

    @Test
    fun `toLong returns the toLong of the Int with which it was created`() {
        val integer = 98765

        val positiveInteger = PositiveInt(integer)

        assertEquals(integer.toLong(), positiveInteger.toLong())
    }

    @Test
    fun `toShort returns the toShort of the Int with which it was created`() {
        val integer = 13123

        val positiveInteger = PositiveInt(integer)

        assertEquals(integer.toShort(), positiveInteger.toShort())
    }

    @Test
    fun `compareTo returns the compareTo of the Int with which it was created`() {
        val firstInt = 23
        val secondInt = 12

        val firstPositive = PositiveInt(firstInt)
        val secondPositive = PositiveInt(secondInt)

        assertTrue(firstPositive > secondPositive)
    }

    @Test
    fun `Adding a PositiveInt with another PositiveInt returns the sum of them`() {
        val first = PositiveInt(23)
        val second = PositiveInt(7876)

        val expected = first.toInt() + second.toInt()
        assertEquals(expected, (first + second).toInt())
    }

    @Test
    fun `Subtracting a PositiveInt from another PositiveInt returns the difference`() {
        val first = PositiveInt(34)
        val second = PositiveInt(1)

        val expected = first.toInt() - second.toInt()
        assertEquals(expected, (first - second).toInt())
    }

    @Test
    fun `Can't subtract a bigger or equal PositiveInt from a PositiveInt`() {
        val first = PositiveInt(42)
        val second = PositiveInt(98934)

        val exception = assertFailsWith<TooBigToSubtractException> {
            first - second
        }

        assertEquals(first, exception.minuend)
        assertEquals(second.toInt(), exception.subtrahend)
    }

    @Test
    fun `Can subtract an Int smaller than the PositiveInt`() {
        val result = PositiveInt(2) - (-5)

        assertEquals(PositiveInt(7), result)
    }

    @Test
    fun `times() multiplies the two PositiveInts`() {
        val first = PositiveInt(2)
        val second = PositiveInt(5)

        val expected = PositiveInt(10)
        assertEquals(expected,first * second)
    }

    @Test
    fun `div() divides the two PositiveInts`() {
        val first = PositiveInt(11)
        val second = PositiveInt(2)

        val expected = PositiveInt(5)
        assertEquals(expected, first / second)
    }

    @Test
    fun `rem() calculates the remainder between the two PositiveInts`() {
        val first = PositiveInt(4)
        val second = PositiveInt(3)

        val expected = PositiveInt(1)
        assertEquals(expected, first % second)
    }

    @Test
    fun `inc() increments the PositiveInt`() {
        var number = PositiveInt(23)

        number++

        val expected = PositiveInt(24)
        assertEquals(expected, number)
    }

    @Test
    fun `dec() decrements the PositiveInt`() {
        var number = PositiveInt(43)

        number--

        val expected = PositiveInt(42)
        assertEquals(expected, number)
    }

    @Test
    fun `Can't decrement PositiveInt from Int 1`() {
        var number = PositiveInt(1)

        assertFailsWith<CantDecrementException> {
            number--
        }
    }

    @Test
    fun `rangeTo() creates a range that corresponds to the same ComparableRange`() {
        val first = PositiveInt(1)
        val second = PositiveInt(5)

        val range = first..second

        assertTrue(first in range)
        assertTrue(second in range)
        assertTrue(PositiveInt(3) in range)
    }
    
    @Test
    fun `Can't increment past the maximum value`() {
        var number = PositiveInt.MAX_VALUE

        assertFailsWith<PositiveIntOverflowException> {
            number++
        }

        assertEquals(number, PositiveInt.MAX_VALUE)
    }

    @Test
    fun `Can't sum past the maximum value`() {
        val first = PositiveInt.MAX_VALUE
        val second = PositiveInt(1)

        assertFailsWith<PositiveIntOverflowException> {
            first + second
        }
    }

    @Test
    fun `Can't multiply past the maximum value`() {
        val first = PositiveInt.MAX_VALUE
        val second = PositiveInt.MAX_VALUE

        assertFailsWith<PositiveIntOverflowException> {
            first * second
        }
    }
}

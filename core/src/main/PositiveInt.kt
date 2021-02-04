class PositiveInt(private val value: Int) : Number(), Comparable<PositiveInt> {

    init {
        assertPositive(value)
    }

    private fun assertPositive(number: Int) {
        if (number <= 0) {
            throw NonPositiveNumberException(number)
        }
    }

    override fun toByte() = value.toByte()
    override fun toChar() = value.toChar()
    override fun toDouble() = value.toDouble()
    override fun toFloat() = value.toFloat()
    override fun toInt() = value
    override fun toLong() = value.toLong()
    override fun toShort() = value.toShort()

    override operator fun compareTo(other: PositiveInt) = toInt().compareTo(other.toInt())

    operator fun plus(other: PositiveInt): PositiveInt {
        try {
            return PositiveInt(Math.addExact(toInt(), other.toInt()))
        } catch (e: ArithmeticException) {
            throw PositiveIntOverflowException()
        }
    }

    operator fun minus(other: Int): PositiveInt {
        try {
            return PositiveInt(toInt() - other)
        } catch (e: NonPositiveNumberException) {
            throw TooBigToSubtractException(this, other)
        }
    }

    operator fun minus(other: PositiveInt): PositiveInt = minus(other.toInt())

    operator fun times(other: PositiveInt): PositiveInt {
        try {
            return PositiveInt(Math.multiplyExact(toInt(), other.toInt()))
        } catch (e: ArithmeticException) {
            throw PositiveIntOverflowException()
        }
    }

    operator fun div(other: PositiveInt) = PositiveInt(toInt() / other.toInt())

    operator fun rem(other: PositiveInt) = PositiveInt(toInt() % other.toInt())

    operator fun inc(): PositiveInt {
        try {
            return PositiveInt(Math.incrementExact(toInt()))
        } catch (e: ArithmeticException) {
            throw PositiveIntOverflowException()
        }
    }

    operator fun dec(): PositiveInt {
        try {
            return PositiveInt(value.dec())
        } catch (e: NonPositiveNumberException) {
            throw CantDecrementException()
        }
    }

    override fun equals(other: Any?): Boolean =
        other is PositiveInt && toInt() == other.toInt()

    override fun hashCode(): Int = toInt()

    override fun toString() = "PositiveInt($value)"

    companion object {
        val MAX_VALUE: PositiveInt = PositiveInt(Int.MAX_VALUE)
    }
}

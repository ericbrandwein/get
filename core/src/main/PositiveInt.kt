class PositiveInt private constructor(
    private val value: Int) : Number(), Comparable<PositiveInt> {

    override fun toByte() = value.toByte()
    override fun toChar() = value.toChar()
    override fun toDouble() = value.toDouble()
    override fun toFloat() = value.toFloat()
    override fun toInt() = value
    override fun toLong() = value.toLong()
    override fun toShort() = value.toShort()

    override operator fun compareTo(other: PositiveInt) = value.compareTo(other.value)

    operator fun plus(other: PositiveInt) = fromInt(toInt() + other.toInt())

    operator fun minus(other: PositiveInt): PositiveInt {
        try {
            return fromInt(toInt() - other.toInt())
        } catch (e: NonPositiveNumberException) {
            throw TooBigToSubtractException(this, other)
        }
    }

    operator fun times(other: PositiveInt) = fromInt(toInt() * other.toInt())

    operator fun div(other: PositiveInt) = fromInt(toInt() / other.toInt())

    operator fun rem(other: PositiveInt) = fromInt(toInt() % other.toInt())

    operator fun inc() = fromInt(value.inc())

    operator fun dec(): PositiveInt {
        try {
            return fromInt(value.dec())
        } catch (e: NonPositiveNumberException) {
            throw CantDecrementException()
        }
    }

    override fun equals(other: Any?): Boolean =
        other is PositiveInt && toInt() == other.toInt()

    override fun hashCode(): Int = toInt()

    override fun toString() = "PositiveInt($value)"

    companion object {

        fun fromInt(number: Int): PositiveInt {
            assertPositive(number)
            return PositiveInt(number)
        }

        private fun assertPositive(number: Int) {
            if (number <= 0) {
                throw NonPositiveNumberException(number)
            }
        }
    }
}

open class PositiveIntException(message: String) : Exception(message)

class NonPositiveNumberException(val number: Number) : PositiveIntException(
    "Can't create a PositiveInt from non-positive number $number.")

class TooBigToSubtractException(
    val minuend: PositiveInt, val subtrahend: PositiveInt
) : PositiveIntException(
    "Can't do $minuend - $subtrahend, it would result in a non-positive number.")

class CantDecrementException : PositiveIntException(
    "Can't decrement a PositiveInt past 1.")

class PositiveIntOverflowException : PositiveIntException(
    "This operation would result in a value greater than the maximum for PositiveInt.")

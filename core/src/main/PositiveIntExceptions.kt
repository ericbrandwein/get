class NonPositiveNumberException(val number: Number) : IllegalArgumentException(
    "Can't create a PositiveInt from non-positive number $number.")

class TooBigToSubtractException(
    val minuend: PositiveInt, val subtrahend: PositiveInt
) : IllegalArgumentException(
    "Can't do $minuend - $subtrahend, it would result in a non-positive number.")

class CantDecrementException : Exception("Can't decrement a PositiveInt past 1.")

class PositiveIntOverflowException : ArithmeticException(
    "This operation would result in a value greater than the maximum for PositiveInt.")

package combat

import PositiveInt

class TooManyArmiesMovedException(val armies: PositiveInt) : Exception(
    "Can't move $armies armies, it would be more than " +
    "the maximum of ${Attack.MAX_CONQUERING_ARMIES}."
)

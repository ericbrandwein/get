package combat

import PositiveInt

class TooManyArmiesContestedException(val armies: PositiveInt) : IllegalArgumentException(
    "Can't contest $armies armies, there's not enough dice rolls to decide who wins.")

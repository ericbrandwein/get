package combat

class TooManyArmiesContestedException(val armies: Int) : IllegalArgumentException(
    "Can't contest $armies armies, there's not enough dice rolls to decide who wins.")

class NonPositiveArmiesContestedException(val armies: Int) : IllegalArgumentException(
    "Can't contest non-positive amount of armies $armies.")

package countries

class NonPositiveArmiesException(val armies: Int) : IllegalArgumentException(
    "The amount of armies should be positive, it was $armies instead.")

class TooManyArmiesRemovedException(val armies: Int) :
    IllegalArgumentException("Can't leave the country with less than one army.")

package occupations

import PositiveInt

class TooManyArmiesRemovedException(val armies: PositiveInt) :
    IllegalArgumentException("Can't leave the country with less than one army.")

package gamelogic.occupations

import Country
import PositiveInt

class TooManyArmiesRemovedException(val armies: PositiveInt) :
    IllegalArgumentException("Can't leave the country with less than one army.")

class EmptyCountryException(val country: Country) :
    Exception("Country $country is not occupied.")


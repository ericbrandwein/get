package gamelogic.map

import Country
import java.lang.Exception

class NonExistentCountryException(val country: Country) :
    Exception("The country $country doesn't exist.")

class CountryAlreadyExistsException(val country: Country) :
    Exception("The country $country already exists.")

class SameCountryBorderException(val country: Country) :
    Exception("Country $country can't be bordering with itself.")

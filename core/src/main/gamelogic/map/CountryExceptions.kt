package gamelogic.map

import java.lang.Exception

class NonExistentCountryException(val country: String) :
    Exception("The country $country doesn't exist.")

class CountryAlreadyExistsException(val country: String) :
    Exception("The country $country already exists.")

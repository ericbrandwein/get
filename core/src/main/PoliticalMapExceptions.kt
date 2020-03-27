import java.lang.Exception

class NonExistentCountryException(val country: String) :
    Exception("The country $country doesn't exist.")

class NonExistentContinentException(val continent: String) :
    Exception("The continent $continent doesn't exist.")

class CountryAlreadyExistsException(val country: String) :
    Exception("The country $country doesn't exist.")
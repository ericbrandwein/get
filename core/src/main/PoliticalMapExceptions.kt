import java.lang.Exception

class NonExistentCountryException(val country: String) :
    Exception("The country $country doesn't exist.")

class NonExistentContinentException(val continent: Continent) :
    Exception("The continent $continent.name doesn't exist.")

class ContinentAlreadyExistsException(val continent: Continent) :
        Exception("The continent $continent.name already exist.")


class CountryAlreadyExistsException(val country: String) :
    Exception("The country $country already exist.")
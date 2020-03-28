package armies

import Country
import java.lang.IllegalArgumentException

class UnoccupiedCountryException(val country: Country) :
    Exception("The country $country has no occupants.")

class CountryAlreadyOccupiedByPlayerException(
    val country: Country,
    val player: Player
) : Exception("The country $country is already occupied by player $player.")

class NonPositiveAmountAddedException(val amount: Int) :
    IllegalArgumentException("Can't add the non-positive amount $amount " +
        "of armies to a country.")

class CantOccupyWithNoArmiesException(val amount: Int) :
    IllegalArgumentException("Can't occupy a country with non-positive " +
        "amount of armies $amount.")

class CantRemoveMoreArmiesThanAvailableException(
    val country: Country,
    val originalAmount: Int,
    val amountRemoved: Int
) : Exception("Can't remove $amountRemoved armies from country $country " +
    "with $originalAmount armies.")

class NonPositiveAmountRemovedException(val amountRemoved: Int) :
    IllegalArgumentException("Can't remove non-positive amount of armies " +
        "$amountRemoved from a country.")

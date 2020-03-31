package occupations

import Country
import Player

class NotOccupyingPlayerException(val player: Player) :
    Exception("Player $player is not occupying this country.")

class CantShareCountryWithYourselfException(val player: Player) :
    Exception("Player $player can't share a country with itself.")

class NonPositiveArmiesException(val armies: Int) :
    Exception("Can't occupy a country with non-positive amount of armies $armies.")

class PlayerAlreadyOccupiesCountryException(val player: Player) : Exception(
    "Can't occupy a country with the same player $player that is already occupying it.")

class NonPositiveArmiesAddedException(val armies: Int) :
    Exception("Can't add the non-positive amount of armies $armies.")

class NonPositiveArmiesRemovedException(val armies: Int) :
    Exception("Can't remove the non-positive amount of armies $armies.")

class TooManyArmiesRemovedException(val currentArmies: Int, val removed: Int) :
    Exception("Can't remove $removed armies when there are only $currentArmies armies," +
        " it would leave the country with less than one army.")

class CantRemoveOnlyOccupierException(val country: Country, val player: Player) :
    Exception("Can't remove $player from $country; they are the only player remaining.")

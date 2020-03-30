package armies

import Player

class NotOccupyingPlayerException(val player: Player) :
    Exception("Player $player is not occupying this country.")

class CantShareCountryWithYourselfException(val player: Player) :
    Exception("Player $player can't share a country with itself.")

class NonPositiveArmiesException(val armies: Int) :
    Exception("Can't occupy a country with non-positive amount of armies $armies.")

class PlayerAlreadyOccupiesCountryException(val player: Player) : Exception(
    "Can't occupy a country with the same player $player that is already occupying it.")

package gamelogic

import Country
import Player
import gamelogic.occupations.CountryOccupations

class CountryIsNotOccupiedByPlayerException(val country: Country, val player: Player) :
    Exception("Country $country is not occupied by $player.")
{
    fun assertPlayerOccupiesCountryIn(occupations: CountryOccupations) {
        if (occupations.occupierOf(country) != player) {
            throw this
        }
    }
}

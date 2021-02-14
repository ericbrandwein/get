package gamelogic

import Country
import Player
import PositiveInt
import gamelogic.occupations.CountryOccupations

class CountryReinforcement(val country: Country, val armies: PositiveInt) {
    fun apply(player: Player, occupations: CountryOccupations) {
        CountryIsNotOccupiedByPlayerException(country, player)
            .assertPlayerOccupiesCountryIn(occupations)
        occupations.addArmies(country, armies)
    }
}

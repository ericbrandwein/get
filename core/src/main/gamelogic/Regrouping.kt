package gamelogic

import Country
import PositiveInt
import gamelogic.occupations.CountryOccupations

/**
 * preconditions (checked in [Referee.validateRegroupings] method):
 *      1. `regroupings.distinctBy { it.from }.count() == regroupings.count()`
 *      2. occupier of from and to is the same as the currentPlayer
 */
class Regrouping(val from: Country, val to: Country, val armies: PositiveInt) {
    fun validate(gameInfo: GameInfo) {
        if (gameInfo.occupations.armiesOf(from) <= armies.toInt()) {
            throw Exception(
                "Cannot move ${armies.toInt()} armies if they are not available in country")
        }
        CountriesAreNotBorderingException(from, to)
            .assertAreBorderingIn(gameInfo.politicalMap)
    }

    fun apply(occupations: CountryOccupations) {
        occupations.removeArmies(from, armies)
        occupations.addArmies(to, armies)
    }
}

package gamelogic

import Country
import gamelogic.map.PoliticalMap

class CountriesAreNotBorderingException(val from: Country, val to: Country) :
    Exception("Countries $from and $to are not bordering.")
{
    fun assertAreBorderingIn(politicalMap: PoliticalMap) {
        if (!politicalMap.areBordering(from, to)) {
            throw this
        }
    }
}

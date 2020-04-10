package countries

import Country
import Player
import PositiveInt

class Occupation(val country: Country, val occupier: Player, armies: PositiveInt) {

    private var mutableArmies: PositiveInt = armies
    val armies get() = mutableArmies

    fun addArmies(added: PositiveInt) {
        mutableArmies += added
    }

    fun removeArmies(removed: PositiveInt) {
        assertCanRemove(removed)
        mutableArmies -= removed
    }

    private fun assertCanRemove(removed: PositiveInt) {
        if (removed >= armies) {
            throw TooManyArmiesRemovedException(removed)
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is Occupation &&
            other.country == country &&
            other.occupier == occupier &&
            other.armies == armies
    }

    override fun hashCode(): Int {
        var result = country.hashCode()
        result = 31 * result + occupier.hashCode()
        result = 31 * result + armies.hashCode()
        return result
    }

    override fun toString(): String {
        return "Occupation on $country by $occupier with $armies armies"
    }

}

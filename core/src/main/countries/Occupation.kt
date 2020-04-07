package countries

import Country
import Player

class Occupation(val country: Country, val occupier: Player, armies: Int) {

    private var mutableArmies: Int = armies
    val armies get() = mutableArmies

    init {
        assertPositiveArmies(mutableArmies)
    }

    private fun assertPositiveArmies(added: Int) {
        if (added <= 0) {
            throw NonPositiveArmiesException(added)
        }
    }

    fun addArmies(added: Int) {
        assertPositiveArmies(added)
        mutableArmies += added
    }

    fun removeArmies(removed: Int) {
        assertPositiveArmies(removed)
        assertCanRemove(removed)
        mutableArmies -= removed
    }

    private fun assertCanRemove(removed: Int) {
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
        result = 31 * result + armies
        return result
    }

    override fun toString(): String {
        return "Occupation on $country by $occupier with $armies armies"
    }

}

package countries

import Country
import Player

data class Occupation(
    val country: Country, val occupier: Player, private var mutableArmies: Int) {

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
}

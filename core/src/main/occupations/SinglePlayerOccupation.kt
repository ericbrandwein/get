package occupations

import Player

/**
 * An Occupation with only one player occupying the country,
 */
class SinglePlayerOccupation(
    val occupier: Player, private var mutableArmies: Int) : Occupation {

    val armies get() = mutableArmies

    init {
        assertPositiveArmies(armies)
    }

    private fun assertPositiveArmies(armies: Int) {
        if (armies <= 0) {
            throw NonPositiveArmiesException(armies)
        }
    }

    fun addArmies(added: Int) {
        assertPositiveAmountAdded(added)
        mutableArmies += added
    }

    private fun assertPositiveAmountAdded(added: Int) {
        if (added <= 0) {
            throw NonPositiveArmiesAddedException(added)
        }
    }

    fun removeArmies(removed: Int) {
        assertPositiveAmountRemoved(removed)
        assertSufficientArmiesAvailableForRemoval(removed)
        mutableArmies -= removed
    }

    private fun assertPositiveAmountRemoved(removed: Int) {
        if (removed <= 0) {
            throw NonPositiveArmiesRemovedException(removed)
        }
    }

    private fun assertSufficientArmiesAvailableForRemoval(removed: Int) {
        if (removed >= armies) {
            throw TooManyArmiesRemovedException(armies, removed)
        }
    }

    override fun accept(visitor: OccupationVisitor) = visitor.visit(this)
}

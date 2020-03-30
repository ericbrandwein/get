package armies

import Player

class SinglePlayerOccupation(val occupier: Player, private var mutableArmies: Int) : Occupation {

    val armies get() = mutableArmies

    init {
        assertPositiveArmies(armies)
    }

    private fun assertPositiveArmies(armies: Int) {
        if (armies <= 0) {
            throw NonPositiveArmiesException(armies)
        }
    }

    override fun occupy(occupier: Player, armies: Int): SinglePlayerOccupation {
        assertNotOccupyingWithSamePlayer(occupier)
        return super.occupy(occupier, armies)
    }

    override fun occupy(
        firstPlayer: Player, secondPlayer: Player, firstArmies: Int, secondArmies: Int
    ): SharedOccupation {
        assertNotOccupyingWithSamePlayer(firstPlayer)
        assertNotOccupyingWithSamePlayer(secondPlayer)
        return super.occupy(firstPlayer, secondPlayer, firstArmies, secondArmies)
    }

    private fun assertNotOccupyingWithSamePlayer(occupier: Player) {
        if (occupier == this.occupier) {
            throw PlayerAlreadyOccupiesCountryException(occupier)
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
}

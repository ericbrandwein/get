package gamelogic.occupations

import Country
import Player
import PositiveInt
import TooBigToSubtractException

interface Occupation {
    val isEmpty: Boolean
    val country: Country
    val occupier: Player
    val armies: PositiveInt

    fun addArmies(added: PositiveInt)

    fun removeArmies(removed: PositiveInt)
}

class PlayerOccupation(
    override val country: Country, override val occupier: Player, armies: PositiveInt
) : Occupation {

    override val isEmpty = false
    private var mutableArmies: PositiveInt = armies
    override val armies get() = mutableArmies

    override fun addArmies(added: PositiveInt) {
        mutableArmies += added
    }

    override fun removeArmies(removed: PositiveInt) {
        try {
            mutableArmies -= removed
        } catch (e: TooBigToSubtractException) {
            throw TooManyArmiesRemovedException(removed)
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is PlayerOccupation &&
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

class NoOccupation : Occupation {
    override val isEmpty = true
    override val country: Country
        get() = TODO("Not yet implemented")
    override val occupier: Player
        get() = TODO("Not yet implemented")
    override val armies: PositiveInt
        get() = PositiveInt(0)

    override fun addArmies(added: PositiveInt) {
        TODO("Not yet implemented")
    }

    override fun removeArmies(removed: PositiveInt) {
        TODO("Not yet implemented")
    }

}

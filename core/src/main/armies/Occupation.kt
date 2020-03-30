package armies

import Player


/**
 * Represents an occupation of some players in a country.
 *
 * The country can be occupied or not, can be shared or not, and can be occupied by other
 * players.
 */
interface Occupation {
    fun isOccupied() = true
    fun isShared() = false

    fun occupy(occupier: Player, armies: Int): SinglePlayerOccupation =
        SinglePlayerOccupation(occupier, armies)

    fun occupy(
        firstPlayer: Player, firstArmies: Int, secondPlayer: Player,
        secondArmies: Int) =
        SharedOccupation(firstPlayer, firstArmies, secondPlayer, secondArmies)
}

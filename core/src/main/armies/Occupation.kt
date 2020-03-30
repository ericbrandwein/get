package armies

import Player

interface Occupation {
    fun isOccupied() = true
    fun isShared() = false

    fun occupy(occupier: Player, armies: Int): SinglePlayerOccupation =
        SinglePlayerOccupation(occupier, armies)

    fun occupy(
        firstPlayer: Player, secondPlayer: Player, firstArmies: Int, secondArmies: Int) =
        SharedOccupation(firstPlayer, secondPlayer, firstArmies, secondArmies)
}

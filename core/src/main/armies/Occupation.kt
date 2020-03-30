package armies

import Player

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

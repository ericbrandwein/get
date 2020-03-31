package occupations

import Player

/**
 * Checks if the player occupies an Occupation.
 */
class PlayerOccupationChecker(private val player: Player) : OccupationVisitor {
    private var playerOccupationFlag = false

    fun doesPlayerOccupy(occupation: Occupation): Boolean {
        occupation.accept(this)
        return playerOccupationFlag
    }

    override fun visit(occupation: NoOccupation) {
        playerOccupationFlag = false
    }

    override fun visit(occupation: SinglePlayerOccupation) {
        playerOccupationFlag = player == occupation.occupier
    }

    override fun visit(occupation: SharedOccupation) {
        playerOccupationFlag = player in occupation.occupiers
    }
}

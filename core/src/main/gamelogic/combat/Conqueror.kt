package gamelogic.combat

import PositiveInt

interface Conqueror {
    fun armiesToMove(remainingAttackingArmies: PositiveInt): PositiveInt
}

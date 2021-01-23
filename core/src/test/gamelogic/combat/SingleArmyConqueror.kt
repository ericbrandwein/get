package gamelogic.combat

import PositiveInt

class SingleArmyConqueror : Conqueror {
    override fun armiesToMove(remainingAttackingArmies: PositiveInt) = PositiveInt(1)
}

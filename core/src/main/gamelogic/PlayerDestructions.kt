package gamelogic

import Player

class PlayerDestructions {
    private var destroyed: MutableMap<Player, Player> = mutableMapOf()

    fun isDestroyed(player: Player) = player in destroyed

    fun destroy(destroyed: Player, destroyer: Player) {
        assertPlayerIsNotDestroyed(destroyed)
        this.destroyed[destroyed] = destroyer
    }

    private fun assertPlayerIsNotDestroyed(player: Player) {
        if (player in destroyed) {
            throw PlayerAlreadyDestroyedException(player)
        }
    }

    fun destroyerOf(player: Player) = destroyed.getValue(player)
}


class PlayerAlreadyDestroyedException(val player: Player) :
    Exception("Player $player is already destroyed.")

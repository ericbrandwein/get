package gamelogic

enum class GameState {
    AddArmies {
        override fun next(gameInfo: GameInfo): GameState {
            return Attack
        }
    },
    Attack {
        override fun next(gameInfo: GameInfo): GameState {
            return Regroup
        }
    },
    Regroup {
        override fun next(gameInfo: GameInfo): GameState {
            gameInfo.playerIterator.next()
            return AddArmies
        }
    };

    abstract fun next(gameInfo: GameInfo): GameState
}

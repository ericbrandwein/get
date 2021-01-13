import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input

abstract class GameState(game: Kamchatka) {
    abstract val game: Kamchatka

    abstract fun render()
    abstract fun keyDown(keycode: Int)
}


class StateReady(override val game: Kamchatka) : GameState(game) {

    override fun render() {
        game.batch.begin()
        game.message.draw(game.batch, "Press s to start, q to quit", 1F, 1F);
        game.batch.end()
    }
    override fun keyDown(keycode: Int) {

        when (keycode) {
            Input.Keys.Q -> Gdx.app.exit()
            Input.Keys.S -> game.state = StateRunning(game)
        }
    }
}

class StateRunning(override val game :Kamchatka):GameState(game) {

    override fun render() {
        game.batch.begin()
        game.batch.draw(game.worldmap,
            -game.worldmap.width.toFloat()/2,
            -game.worldmap.height.toFloat()/2
        )

        game.batch.end()

    }

    override fun keyDown(keycode: Int) {
        when (keycode) {
            Input.Keys.Q -> game.state = StateReady(game)
        }
    }
}

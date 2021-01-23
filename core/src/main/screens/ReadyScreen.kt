package screens

import Kamchatka
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.BitmapFont

class ReadyScreen(private val game: Kamchatka) : KamchatkaScreen(game) {
    private val message = BitmapFont()

    override fun render(delta: Float) {
        super.render(delta)
        game.batch.begin()
        message.draw(game.batch, "Press s to start, q to quit", 1F, 1F)
        game.batch.end()
    }

    override fun keyDown(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.Q -> Gdx.app.exit()
            Input.Keys.S -> game.setKamchatkaScreen(RunningScreen(game))
        }
        return true
    }
}

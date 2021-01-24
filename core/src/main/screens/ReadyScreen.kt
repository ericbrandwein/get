package screens

import Kamchatka
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import screens.running.RunningScreen

class ReadyScreen(private val game: Kamchatka) : KamchatkaScreen(game) {
    private val message = BitmapFont()
    override var viewport: Viewport = FitViewport(800F, 480F, game.camera)
    override var inputProcessor: InputProcessor = object: InputAdapter() {
        override fun keyDown(keycode: Int): Boolean {
            when (keycode) {
                Input.Keys.Q -> Gdx.app.exit()
                Input.Keys.S -> game.setKamchatkaScreen(RunningScreen(game))
            }
            return true
        }
    }

    override fun render(delta: Float) {
        super.render(delta)
        game.batch.begin()
        message.draw(game.batch, "Press s to start, q to quit", 1F, 1F)
        game.batch.end()
    }
}

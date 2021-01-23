package screens

import Kamchatka
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.utils.viewport.Viewport


abstract class KamchatkaScreen(
    private val game: Kamchatka
) : ScreenAdapter() {
    abstract var viewport: Viewport
    abstract var inputProcessor: InputProcessor

    override fun render(delta: Float) {
        super.render(delta)
        Gdx.gl.glClearColor(1f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        game.batch.projectionMatrix = game.camera.combined
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        viewport.update(width, height)
    }
}





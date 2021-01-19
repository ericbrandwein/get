import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Game
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.Viewport
import com.badlogic.gdx.utils.viewport.FitViewport


class Kamchatka : Game() {
    lateinit var batch: SpriteBatch
    lateinit var camera: OrthographicCamera
    lateinit var viewport: Viewport

    
    override fun create() {
        batch = SpriteBatch()
        camera = OrthographicCamera()
        viewport = FitViewport(800F, 480F, camera)
        val screen = ReadyScreen(this)
        setKamchatkaScreen(screen)
    }

    override fun dispose() {
        batch.dispose()
    }

    fun setKamchatkaScreen(screen: KamchatkaScreen) {
        super.setScreen(screen)
        Gdx.input.inputProcessor = screen
    }
}

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.Viewport
import screens.KamchatkaScreen
import screens.ReadyScreen


class Kamchatka : Game() {
    lateinit var batch: SpriteBatch
    lateinit var camera: OrthographicCamera

    override fun create() {
        batch = SpriteBatch()
        camera = OrthographicCamera()
        setKamchatkaScreen(ReadyScreen(this))
    }

    override fun dispose() {
        batch.dispose()
    }

    fun setKamchatkaScreen(screen: KamchatkaScreen) {
        setScreen(screen)
        Gdx.input.inputProcessor = screen.inputProcessor
    }

}

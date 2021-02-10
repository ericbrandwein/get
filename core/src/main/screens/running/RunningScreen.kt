package screens.running

import Kamchatka
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import screens.KamchatkaScreen

private const val MAP_FILE_NAME = "mapa.png"

class RunningScreen(game: Kamchatka) : KamchatkaScreen(game) {
    private val assetManager = AssetManager()
    override val viewport: Viewport
    private val stage: Stage
    override val inputProcessor: InputProcessor

    init {
        assetManager.load(MAP_FILE_NAME, Texture::class.java)
        val worldmapTexture = assetManager.finishLoadingAsset<Texture>(MAP_FILE_NAME)
        viewport = FitViewport(
            worldmapTexture.width.toFloat(),
            worldmapTexture.height.toFloat(),
            game.camera
        )
        stage = WorldmapStage(assetManager, worldmapTexture, viewport)
        inputProcessor = stage
    }

    override fun render(delta: Float) {
        super.render(delta)

        stage.act(delta)
        stage.draw()
    }

    override fun dispose() {
        super.dispose()
        assetManager.dispose()
    }
}


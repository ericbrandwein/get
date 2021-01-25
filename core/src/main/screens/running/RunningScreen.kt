package screens.running

import Kamchatka
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import screens.KamchatkaScreen


class RunningScreen(game: Kamchatka) : KamchatkaScreen(game) {
    private val assetManager = AssetManager()
    private val worldmapTexture: Texture
    override val viewport: Viewport
    private val stage: Stage
    override val inputProcessor: InputProcessor

    init {
        val mapFileName = "mapa.png"
        assetManager.load(mapFileName, Texture::class.java)
        worldmapTexture = assetManager.finishLoadingAsset<Texture>(mapFileName)
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


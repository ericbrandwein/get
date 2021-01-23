package screens.running

import Kamchatka
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import screens.KamchatkaScreen


class RunningScreen(game: Kamchatka) : KamchatkaScreen(game) {
    private val worldmapTexture = Texture("mapa.png")
    override var viewport: Viewport = FitViewport(
        worldmapTexture.width.toFloat(),
        worldmapTexture.height.toFloat(),
        game.camera
    )
    private val stage: Stage = WorldmapStage(worldmapTexture, viewport)
    override var inputProcessor: InputProcessor = stage

    override fun render(delta: Float) {
        super.render(delta)

        stage.act(delta)
        stage.draw()
    }

    override fun dispose() {
        super.dispose()
        worldmapTexture.dispose()
    }
}


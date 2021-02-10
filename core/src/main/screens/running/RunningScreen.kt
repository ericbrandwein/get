package screens.running

import Country
import Kamchatka
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import gamelogic.Referee
import screens.KamchatkaScreen

class RunningScreen(
    game: Kamchatka, referee: Referee, countryColors: CountryColors
) : KamchatkaScreen(game), CountrySelectionListener {
    private val assetManager = AssetManager()
    override val viewport: Viewport
    private val stage: WorldmapStage
    override val inputProcessor: InputProcessor

    init {
        assetManager.load(MAP_IMAGE_FILE_NAME, Texture::class.java)
        val worldmapTexture =
            assetManager.finishLoadingAsset<Texture>(MAP_IMAGE_FILE_NAME)
        viewport = FitViewport(
            worldmapTexture.width.toFloat(),
            worldmapTexture.height.toFloat(),
            game.camera
        )
        stage = WorldmapStage(viewport, assetManager, worldmapTexture, countryColors)
        stage.countrySelectionListener = this
        inputProcessor = stage
    }

    override fun onCountrySelected(country: Country) {
        println("Country $country was selected")
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

    companion object {
        private const val MAP_IMAGE_FILE_NAME = "mapa.png"
    }
}


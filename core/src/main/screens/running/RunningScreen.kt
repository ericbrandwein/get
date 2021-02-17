package screens.running

import Country
import Kamchatka
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.Input
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import gamelogic.Referee
import screens.KamchatkaScreen
import screens.ReadyScreen

class RunningScreen(
    private val game: Kamchatka, referee: Referee, countryColors: CountryColors
) : KamchatkaScreen(game), CountrySelectionListener {
    private val assetManager = AssetManager()
    override val viewport: Viewport
    private val stage: WorldmapStage
    override val  inputProcessor = InputMultiplexer()

    private val keyInputProcessor: InputProcessor = object: InputAdapter() {
        override fun keyDown(keycode: Int): Boolean {
            when (keycode) {
                Input.Keys.Q -> switchToReadyScreen()
            }
            return true
        }
    }

    init {
        assetManager.load(MAP_IMAGE_FILE_NAME, Texture::class.java)
        val worldmapTexture =
            assetManager.finishLoadingAsset<Texture>(MAP_IMAGE_FILE_NAME)
        viewport = FitViewport(
            worldmapTexture.width.toFloat(),
            worldmapTexture.height.toFloat(),
            game.camera
        )
        stage = WorldmapStage(
            viewport, assetManager, worldmapTexture, countryColors, referee.occupations)
        inputProcessor.addProcessor(keyInputProcessor)
        inputProcessor.addProcessor(stage)
    }

    private fun switchToReadyScreen() {
        game.setKamchatkaScreen(ReadyScreen(game))
    }

    override fun onCountrySelected(country: Country) {
        println("Country $country was selected")
    }

    override fun onCountryMouseOver(country: Country) {
        TODO("Not yet implemented")
    }

    override fun onCountryExit(country: Country) {
        TODO("Not yet implemented")
    }

    override fun render(delta: Float) {
        super.render(delta)
        stage.act(delta)
        stage.draw()
    }


    override fun dispose() {
        super.dispose()
        assetManager.dispose()
        stage.dispose()
    }

    companion object {
        private const val MAP_IMAGE_FILE_NAME = "mapa.png"
    }
}


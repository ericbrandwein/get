package screens

import CountrySelector
import Kamchatka
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.utils.viewport.FitViewport

class RunningScreen(private val game: Kamchatka) : KamchatkaScreen(game) {
    private val worldmap: Sprite = Sprite(Texture("mapa.png"))
    private var currentCountry: String? = null
    private val message = BitmapFont()
    private val countrySelector = CountrySelector(worldmap, game.camera)

    init {
        game.viewport = FitViewport(
            worldmap.width,
            worldmap.height,
            game.camera
        )
        worldmap.setPosition(-worldmap.width / 2, -worldmap.height / 2)
    }

    override fun render(delta: Float) {
        super.render(delta)
        game.batch.begin()

        worldmap.draw(game.batch)
        if (currentCountry != null) {
            message.draw(game.batch, currentCountry, 1F, 1F)
        }
        game.batch.end()
    }

    override fun keyDown(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.Q -> game.setKamchatkaScreen(ReadyScreen(game))
        }
        return true
    }

    override fun dispose() {
        super.dispose()
        worldmap.texture.dispose()
    }

    override fun touchDown(
        screenX: Int, screenY: Int, pointer: Int, button: Int
    ): Boolean {
        val country = countrySelector.selectByScreenPosition(screenX, screenY)
        if (country == null) {
            println("There's no country here.")
        } else {
            println("Selected country $country.")
        }
        return true
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        currentCountry = countrySelector.selectByScreenPosition(screenX, screenY)
        return true
    }

}

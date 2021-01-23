package screens

import Kamchatka
import MapColors
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.viewport.FitViewport

class RunningScreen(private val game: Kamchatka) : KamchatkaScreen(game) {
    private val worldmap: Sprite = Sprite(Texture("mapa.png"))
    private val countryColorsMap: Texture = Texture("colores-paises.png")
    private var currentCountry: String? = null
    private val mapColors = MapColors.fromJsonFile("mapa.json")
    private val message = BitmapFont()
    private val worldmapPixmap: Pixmap

    init {
        game.viewport = FitViewport(
            worldmap.width,
            worldmap.height,
            game.camera
        )
        val textureData = countryColorsMap.textureData
        if (!textureData.isPrepared) {
            textureData.prepare()
        }
        worldmapPixmap = textureData.consumePixmap()
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
        val color = getMapColorAtPoint(screenX, screenY)
        if (color in mapColors) {
            val country = mapColors[color]
            println("position: ($screenX, $screenY), country: $country, color: $color.")
        } else {
            println("There's no country in position ($screenX, $screenY).")
        }
        return true
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        val color = getMapColorAtPoint(screenX, screenY)
        currentCountry = mapColors[color]
        return true
    }

    private fun getMapColorAtPoint(screenX: Int, screenY: Int): Color {
        val (actualX, actualY) = screenPositionToWorldMapPosition(screenX, screenY)
        return Color(worldmapPixmap.getPixel(actualX, actualY))
    }

    private fun screenPositionToWorldMapPosition(
        screenX: Int, screenY: Int
    ): Pair<Int, Int> {
        val screenPosition = Vector3(screenX.toFloat(), screenY.toFloat(), 0F)
        val worldPosition = game.camera.unproject(screenPosition)
        // worldPosition starts from the top left,
        // spritePosition starts from the bottom left.
        // This means that the y coordinate must be "inverted"
        return Pair(
            (worldPosition.x - worldmap.x).toInt(),
            (worldmap.height - (worldPosition.y - worldmap.y)).toInt()
        )

    }

}

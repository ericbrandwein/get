import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite

class CountrySelector(private val worldmap: Sprite, private val camera: Camera) {
    private val countryColorsMap: Texture = Texture("colores-paises.png")
    private val countryColorsPixmap: Pixmap
    private val mapColors = MapColors.fromJsonFile("mapa.json")

    init {
        val textureData = countryColorsMap.textureData
        if (!textureData.isPrepared) {
            textureData.prepare()
        }
        countryColorsPixmap = textureData.consumePixmap()
    }

    fun selectByScreenPosition(screenX: Int, screenY: Int): String? {
        val color = getMapColorAtPoint(screenX, screenY)
        return mapColors[color]
    }

    private fun getMapColorAtPoint(screenX: Int, screenY: Int): Color {
        val (actualX, actualY) = screenPositionToSpritePosition(
            screenX, screenY, worldmap, camera
        )
        return Color(countryColorsPixmap.getPixel(actualX, actualY))
    }

}

package screens.running

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture

class CountrySelector {
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

    fun selectByMapPosition(mapX: Float, mapY: Float): String? {
        // Pixmap coordinates start at the top left corner,
        // while the mapX and mapY start at the bottom left corner,
        // meaning we have to invert the y coordinate.
        val pixel = countryColorsPixmap.getPixel(
            mapX.toInt(), countryColorsMap.height - mapY.toInt()
        )
        return mapColors[Color(pixel)]
    }
}

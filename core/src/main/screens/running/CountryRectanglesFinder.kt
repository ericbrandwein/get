package screens.running

import Country
import IntRectangle
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap

fun findCountryRectangles(
    pixmap: Pixmap, countryColors: CountryColors
): Map<Country, IntRectangle> {
    val rectangles = mutableMapOf<Country, IntRectangle>()
    pixmap.forEachIndexed { x, y, pixelColor ->
        if (pixelColor in countryColors) {
            val country = countryColors.getValue(pixelColor)
            rectangles
                .getOrPut(country, { IntRectangle(x, y, 1, 1) })
                .merge(x, y)
        }
    }
    return rectangles
}

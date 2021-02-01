package screens.running.countryImage

import Country
import IntRectangle
import com.badlogic.gdx.graphics.Pixmap
import screens.running.CountryColors
import screens.running.forEachIndexed

/**
 * Finds the minimal [IntRectangle]s that contain all pixels of each [Country] inside
 * the [pixmap].
 */
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

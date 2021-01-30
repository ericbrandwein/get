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

private fun Pixmap.forEachIndexed(block: (Int, Int, Color) -> Unit) {
    for (x in 0..width) {
        for (y in 0..height) {
            block(x, y, Color(getPixel(x, y)))
        }
    }
}

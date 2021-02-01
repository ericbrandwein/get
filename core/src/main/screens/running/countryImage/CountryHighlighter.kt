package screens.running.countryImage

import IntRectangle
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap

val HIGHLIGHT_COLOR = Color(1f, 1f, 1f, 0.5f)

/**
 * Creates a [Pixmap] containing only the pixels from [mapPixmap] inside the [rectangle],
 * transformed so that every pixel with color equal to the [countryColor] is changed to
 * [HIGHLIGHT_COLOR] and every other pixel is [Color.CLEAR].
 */
fun getHighlightedCountryPixmap(
    mapPixmap: Pixmap, rectangle: IntRectangle, countryColor: Color
): Pixmap {
    val result = Pixmap(rectangle.width, rectangle.height, Pixmap.Format.RGBA8888)
    result.setColor(HIGHLIGHT_COLOR)

    for (x in 0 until rectangle.width) {
        for (y in 0 until rectangle.height) {
            val pixelX = rectangle.x + x
            val pixelY = rectangle.y + y
            assertPositionInsidePixmap(mapPixmap, pixelY, pixelX)
            val pixelColor = Color(mapPixmap.getPixel(pixelX, pixelY))
            if (pixelColor == countryColor) {
                result.drawPixel(x, y)
            }
        }
    }

    return result
}

private fun assertPositionInsidePixmap(
    mapPixmap: Pixmap, pixelY: Int, pixelX: Int
) {
    if (pixelX >= mapPixmap.width || pixelY >= mapPixmap.height) {
        throw PixelOutOfBoundsException(pixelX, pixelY)
    }
}

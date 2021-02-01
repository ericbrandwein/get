package screens.running

import IntRectangle
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap

val HIGHLIGHT_COLOR = Color(1f, 1f, 1f, 0.5f)

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
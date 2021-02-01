package screens.running.countryImage

import IntRectangle
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Image

/**
 * An [Image] that corresponds to the position and dimensions of a country in the
 * worldmap Pixmap. It can be [highlight]ed so as to appear brighter.
 */
class CountryImage(
    x: Float, y: Float, private val highlightPixmap: Pixmap
) : Image(Texture(highlightPixmap)) {

    init {
        setPosition(x, y)
        removeHighlight()
    }

    override fun hit(x: Float, y: Float, touchable: Boolean): Actor? =
        if (isPositionInsideCountry(x, y)) this else null

    private fun isPositionInsideCountry(x: Float, y: Float): Boolean {
        val actualY = highlightPixmap.height - y - 1
        val color = Color(highlightPixmap.getPixel(x.toInt(), actualY.toInt()))
        return color != Color.CLEAR
    }

    fun highlight() {
        color = Color.WHITE
    }

    fun removeHighlight() {
        color = Color.CLEAR
    }

    companion object {

        /**
         * @param rectangle The rectangle describing the position and dimensions
         * in the Pixmap where this country is located. Should be relative to the
         * top left corner of the Pixmap.
         */
        fun fromPixmapRectangle(
            countryColorsPixmap: Pixmap, rectangle: IntRectangle, countryColor: Color
        ): CountryImage {
            val highlightPixmap =
                getHighlightedCountryPixmap(countryColorsPixmap, rectangle, countryColor)
            val flippedY = flipY(rectangle.y, countryColorsPixmap, rectangle)
            return CountryImage(
                rectangle.x.toFloat(), flippedY.toFloat(), highlightPixmap)
        }

        /**
         * Coordinates in the rectangle are given from the top left, and an Actor's
         * position is defined from the bottom left, so we have to flip the y axis.
         */
        private fun flipY(y: Int, pixmap: Pixmap, rectangle: IntRectangle) =
            pixmap.height - rectangle.maxY - 1
    }
}

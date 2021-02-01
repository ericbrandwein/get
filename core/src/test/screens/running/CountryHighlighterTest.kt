package screens.running

import GdxTestRunner
import IntRectangle
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse

@RunWith(GdxTestRunner::class)
class CountryHighlighterTest {
    @Test
    fun `Highlighted pixmap has brighter pixel colors`() {
        val pixmap = createPixmap(1, 1)
        val countryColor = Color.RED
        pixmap.setColor(countryColor)
        pixmap.fill()

        val rectangle = IntRectangle(0, 0, 1, 1)

        val result = getHighlightedCountryPixmap(pixmap, rectangle, countryColor)

        val highlightedPixmap = createPixmap(1, 1)
        highlightedPixmap.setColor(HIGHLIGHT_COLOR)
        highlightedPixmap.fill()
        assertPixmapEquals(highlightedPixmap, result)
    }

    @Test
    fun `Highlighted pixmap should have the same dimensions as the rectangle`() {
        val pixmap = createPixmap(2, 2)
        val countryColor = Color.RED
        pixmap.setColor(countryColor)
        pixmap.fill()
        val rectangle = IntRectangle(0, 0, 2, 2)

        val result = getHighlightedCountryPixmap(pixmap, rectangle, countryColor)

        val highlightedPixmap = createPixmap(2, 2)
        highlightedPixmap.setColor(HIGHLIGHT_COLOR)
        highlightedPixmap.fill()
        assertPixmapEquals(highlightedPixmap, result)
    }

    @Test
    fun `Highlighted pixmap should clear the pixels that aren't of the country's color`() {
        val pixmap = createPixmap(2,2)
        pixmap.setColor(Color.BLACK)
        pixmap.fill()
        val countryColor = Color.RED
        pixmap.setColor(countryColor)
        pixmap.drawPixel(1, 1)

        val rectangle = IntRectangle(0, 0, 2, 2)

        val result = getHighlightedCountryPixmap(pixmap, rectangle, countryColor)

        val highlightedPixmap = createPixmap(2, 2)
        highlightedPixmap.setColor(Color.CLEAR)
        highlightedPixmap.fill()
        highlightedPixmap.setColor(HIGHLIGHT_COLOR)
        highlightedPixmap.drawPixel(1, 1)
        assertPixmapEquals(highlightedPixmap, result)
    }

    @Test
    fun `Highlighted pixmap should take the image from the rectangle position`() {
        val pixmap = createPixmap(2,2)
        pixmap.setColor(Color.BLACK)
        pixmap.fill()
        val countryColor = Color.RED
        pixmap.setColor(countryColor)
        pixmap.drawPixel(1, 1)

        val rectangle = IntRectangle(1, 1, 1, 1)

        val result = getHighlightedCountryPixmap(pixmap, rectangle, countryColor)

        val highlightedPixmap = createPixmap(1, 1)
        highlightedPixmap.setColor(HIGHLIGHT_COLOR)
        highlightedPixmap.fill()
        assertPixmapEquals(highlightedPixmap, result)
    }

    @Test
    fun `Can't use a rectangle out of the bounds of the pixmap`() {
        val pixmap = createPixmap(0, 0)
        val rectangle = IntRectangle(0, 0, 1, 1)

        assertFailsWith<PixelOutOfBoundsException> {
            getHighlightedCountryPixmap(pixmap, rectangle, Color.RED)
        }
    }

    private fun assertPixmapEquals(expected: Pixmap, actual: Pixmap) {
        assertEquals(expected.width, actual.width, "Pixmap width doesn't match.")
        assertEquals(expected.height, actual.height, "Pixmap height doesn't match.")
        expected.forEachIndexed { x, y, expectedColor ->
            val actualColor = Color(actual.getPixel(x, y))
            assertEquals(
                expectedColor, actualColor,
                "Pixel ($x, $y) doesn't have the expected color."
            )
        }
    }

    private fun createPixmap(width: Int, height: Int) =
        Pixmap(width, height, Pixmap.Format.RGBA8888)
}

package screens.running.countryImage

import Country
import GdxTestRunner
import IntRectangle
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import junit.framework.TestCase
import org.junit.runner.RunWith
import screens.running.CountryColors
import kotlin.test.Test

@RunWith(GdxTestRunner::class)
class CountryRectanglesFinderTest : TestCase() {
    private val aCountry = "Argentina"
    private val otherCountry = "Chile"
    private val aColor = Color.WHITE
    private val otherColor = Color.BLACK
    private val anotherColor = Color.RED
    private val singleCountryCountryColors: CountryColors =
        CountryColors(mapOf(aCountry to aColor))

    @Test
    fun `Finding in an empty Pixmap should return an empty collection`() {
        val colors = CountryColors(mapOf())
        val pixmap = createPixmap(0, 0)
        val rectangles: Map<Country, IntRectangle> = findCountryRectangles(pixmap, colors)

        assertTrue(rectangles.isEmpty())
    }

    @Test
    fun `Finding in a Pixmap with one pixel of a country color should return a collection of only that country`() {
        val pixmap = createPixmap(1, 1)
        pixmap.setColor(aColor)
        pixmap.drawPixel(0, 0)

        val rectangles: Map<Country, IntRectangle> =
            findCountryRectangles(pixmap, singleCountryCountryColors)

        assertEquals(1, rectangles.size)
        assertTrue(aCountry in rectangles)
        assertEquals(IntRectangle(0, 0, 1, 1), rectangles[aCountry])
    }

    @Test
    fun `Finding in a Pixmap with one pixel of a country color not in the first pixel should correctly detect the country's position`() {
        val pixmap = createPixmap(2, 1)
        pixmap.setColor(aColor)
        pixmap.drawPixel(1, 0)
        pixmap.setColor(otherColor)
        pixmap.drawPixel(0, 0)

        val rectangles: Map<Country, IntRectangle> =
            findCountryRectangles(pixmap, singleCountryCountryColors)

        assertEquals(1, rectangles.size)
        assertTrue(aCountry in rectangles)
        assertEquals(IntRectangle(1, 0, 1, 1), rectangles[aCountry])
    }

    @Test
    fun `Finding in a Pixmap with one pixel of a country color not in the first row should correctly detect the country's position`() {
        val pixmap = createPixmap(1, 2)
        pixmap.setColor(aColor)
        pixmap.drawPixel(0, 1)
        pixmap.setColor(otherColor)
        pixmap.drawPixel(0, 0)

        val rectangles: Map<Country, IntRectangle> =
            findCountryRectangles(pixmap, singleCountryCountryColors)

        assertEquals(1, rectangles.size)
        assertTrue(aCountry in rectangles)
        assertEquals(IntRectangle(0, 1, 1, 1), rectangles[aCountry])
    }

    @Test
    fun `Finding in a Pixmap with a country with two pixels should correctly detect the country's dimensions`() {
        val pixmap = createPixmap(2, 1)
        pixmap.setColor(aColor)
        pixmap.drawLine(0, 0, 1, 0)

        val rectangles: Map<Country, IntRectangle> =
            findCountryRectangles(pixmap, singleCountryCountryColors)

        assertEquals(1, rectangles.size)
        assertTrue(aCountry in rectangles)
        assertEquals(IntRectangle(0, 0, 2, 1), rectangles[aCountry])
    }

    @Test
    fun `Finding in a Pixmap with a country with more than two pixels should correctly detect the country's dimensions`() {
        val width = 10
        val pixmap = createPixmap(width, 1)
        pixmap.setColor(aColor)
        pixmap.drawLine(0, 0, width - 1, 0)

        val rectangles: Map<Country, IntRectangle> =
            findCountryRectangles(pixmap, singleCountryCountryColors)

        assertEquals(1, rectangles.size)
        assertTrue(aCountry in rectangles)
        assertEquals(IntRectangle(0, 0, width, 1), rectangles[aCountry])
    }

    @Test
    fun `Finding in a Pixmap with a country with many pixels not starting from the first row should correctly detect the country's dimensions`() {
        val width = 10
        val pixmap = createPixmap(width, 1)
        pixmap.setColor(otherColor)
        pixmap.fill()
        pixmap.setColor(aColor)
        pixmap.drawLine(1, 0, width - 1, 0)

        val rectangles: Map<Country, IntRectangle> =
            findCountryRectangles(pixmap, singleCountryCountryColors)

        assertEquals(1, rectangles.size)
        assertTrue(aCountry in rectangles)
        assertEquals(IntRectangle(1, 0, width - 1, 1), rectangles[aCountry])
    }

    @Test
    fun `Finding in a Pixmap with many countries in different positions should correctly detect them`() {
        val countryColors =
            CountryColors(mapOf(aCountry to aColor, otherCountry to otherColor))
        val pixmap = createPixmap(10, 10)
        pixmap.setColor(anotherColor)
        pixmap.fill()
        pixmap.setColor(aColor)
        pixmap.drawPixel(0, 0)
        pixmap.drawPixel(5, 2)
        pixmap.setColor(otherColor)
        pixmap.drawPixel(6, 1)
        pixmap.drawPixel(9, 9)

        val rectangles: Map<Country, IntRectangle> =
            findCountryRectangles(pixmap, countryColors)

        assertEquals(2, rectangles.size)
        assertTrue(aCountry in rectangles)
        assertEquals(IntRectangle(0, 0, 6, 3), rectangles[aCountry])
        assertTrue(otherCountry in rectangles)
        assertEquals(IntRectangle(6, 1, 4, 9), rectangles[otherCountry])

    }

    private fun createPixmap(width: Int, height: Int) =
        Pixmap(width, height, Pixmap.Format.RGBA8888)

}

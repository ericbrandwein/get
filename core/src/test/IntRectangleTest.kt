import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IntRectangleTest {
    @Test
    fun `Merging with a point already included should not change anything`() {
        val rectangle = IntRectangle(0, 0, 1, 1)
        rectangle.merge(0, 0)

        assertEquals(IntRectangle(0, 0, 1, 1), rectangle)
    }

    @Test
    fun `Merging with a point with x farther than the width should increment the width`() {
        val rectangle = IntRectangle(0, 0, 1, 1)
        rectangle.merge(1, 0)

        assertEquals(IntRectangle(0, 0, 2, 1), rectangle)
    }

    @Test
    fun `Merging with a point with x smaller than the rectangle position should change position and width`() {
        val rectangle = IntRectangle(1, 0, 1, 1)
        rectangle.merge(0, 0)

        assertEquals(IntRectangle(0, 0, 2, 1), rectangle)
    }


    @Test
    fun `Merging with a point with y farther than the height should increment the height`() {
        val rectangle = IntRectangle(0, 0, 1, 1)
        rectangle.merge(0, 1)

        assertEquals(IntRectangle(0, 0, 1, 2), rectangle)
    }

    @Test
    fun `Merging with a point with y smaller than the rectangle position should change position and height`() {
        val rectangle = IntRectangle(0, 1, 1, 1)
        rectangle.merge(0, 0)

        assertEquals(IntRectangle(0, 0, 1, 2), rectangle)
    }

    @Test
    fun `Point with x and y inside the rectangle is included in it`() {
        val rectangle = IntRectangle(0, 0, 1, 1)

        assertTrue(rectangle.includes(0, 0))
    }

    @Test
    fun `Point with x greater than the maxX is not included in the rectangle`() {
        val rectangle = IntRectangle(0, 0, 1, 1)

        assertFalse(rectangle.includes(1, 0))
    }

    @Test
    fun `Point with x smaller than the rectangle's x is not included in it`() {
        val rectangle = IntRectangle(1, 0, 1, 1)

        assertFalse(rectangle.includes(0, 0))
    }

    @Test
    fun `Point with y greater than the maxY is not included in the rectangle`() {
        val rectangle = IntRectangle(0, 0, 1, 1)

        assertFalse(rectangle.includes(0, 1))
    }

    @Test
    fun `Point with y smaller than the rectangle's y is not included in it`() {
        val rectangle = IntRectangle(0, 1, 1, 1)

        assertFalse(rectangle.includes(0, 0))
    }
}

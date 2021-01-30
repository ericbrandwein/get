import java.lang.Integer.max
import java.lang.Integer.min
import com.badlogic.gdx.math.Rectangle

/**
 * Position is given from the bottom left corner.
 * @see Rectangle
 */
data class IntRectangle(
    var x: Int, var y: Int, var width: Int, var height: Int
) {
    /**
     * The maximum X value that is included in this rectangle.
     * Note that x + width is not included. This makes it useful
     * when using an IntRectangle as a texture region, where the width is defined in
     * pixels.
     */
    private var maxX
        get() = x + width - 1
        set(value) {
            width = value - x + 1
        }

    /**
     * The maximum Y value that is included in this rectangle.
     * Note that y + height is not included. This makes it useful
     * when using an IntRectangle as a texture region, where the height is defined in
     * pixels.
     */
    private var maxY
        get() = y + height - 1
        set(value) {
            height = value - y + 1
        }

    /**
     * Check if this point is included in the rectangle.
     */
    fun includes(x: Int, y: Int) =
        this.x <= x && this.maxX >= x && this.y <= y && this.maxY >= y

    /**
     * Modify this rectangle so that this point is included.
     */
    fun merge(newX: Int, newY: Int) {
        val minX = min(x, newX)
        val maxX = max(maxX, newX)
        x = minX
        this.maxX = maxX

        val minY = min(y, newY)
        val maxY = max(maxY, newY)
        y = minY
        this.maxY = maxY
    }
}

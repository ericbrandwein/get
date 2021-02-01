package screens.running

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap

fun Pixmap.forEachIndexed(block: (Int, Int, Color) -> Unit) {
    for (x in 0..width) {
        for (y in 0..height) {
            block(x, y, Color(getPixel(x, y)))
        }
    }
}

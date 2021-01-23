import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector3

fun screenPositionToSpritePosition(
    screenX: Int, screenY: Int, sprite: Sprite, camera: Camera
): Pair<Int, Int> {
    val screenPosition = Vector3(screenX.toFloat(), screenY.toFloat(), 0F)
    val worldPosition = camera.unproject(screenPosition)
    // worldPosition starts from the top left,
    // spritePosition starts from the bottom left.
    // This means that the y coordinate must be "inverted".
    return Pair(
        (worldPosition.x - sprite.x).toInt(),
        (sprite.height - (worldPosition.y - sprite.y)).toInt()
    )

}

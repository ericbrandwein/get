//import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Game
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.Viewport
import com.badlogic.gdx.utils.viewport.FitViewport


class Kamchatka : Game(), InputProcessor {
    lateinit var batch: SpriteBatch
    lateinit var worldmap: Texture
    lateinit var camera: OrthographicCamera
    lateinit var viewport: Viewport

    
    override fun create() {
        batch = SpriteBatch()
        worldmap = Texture("Sudamerica.png")
        camera = OrthographicCamera()
        viewport = FitViewport(
            worldmap.width.toFloat(),
            worldmap.height.toFloat(),
            camera
        )
        Gdx.input.inputProcessor = this
    }


    
    override fun render() {

        Gdx.gl.glClearColor(1f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        batch.projectionMatrix = camera.combined

        
        batch.begin()
        batch.draw(worldmap,
                   -worldmap.width.toFloat()/2,
                   -worldmap.height.toFloat()/2
        )

        batch.end()

    }

    override fun dispose() {
        worldmap.dispose()
        batch.dispose()
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
    }

    override fun keyDown(keycode: Int): Boolean {
        return false
        TODO("Not yet implemented")
    }

    override fun keyUp(keycode: Int): Boolean {
        return false
        TODO("Not yet implemented")
    }

    override fun keyTyped(character: Char): Boolean {
        return false
        TODO("Not yet implemented")
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        println(" color: ${getMapColorAtPoint(screenX, screenY)}")
        return false
        TODO("Not yet implemented")
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
        TODO("Not yet implemented")
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
        TODO("Not yet implemented")
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
        TODO("Not yet implemented")
    }

    override fun scrolled(amount: Int): Boolean {
        return false
        TODO("Not yet implemented")
    }

    private fun getMapColorAtPoint(x: Int, y: Int) : Color {
        val resizeX = (worldmap.textureData.width.toFloat()/viewport.screenWidth.toFloat())
        val resizeY = (worldmap.textureData.height.toFloat()/viewport.screenHeight.toFloat())
        val x = (resizeX * (x - viewport.screenX).toFloat()).toInt()
        val y = (resizeY * (y- viewport.screenY).toFloat()).toInt()
        worldmap.textureData.prepare()
        return Color(worldmap.textureData.consumePixmap().getPixel(x,y))
    }
}

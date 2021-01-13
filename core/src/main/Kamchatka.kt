//import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.utils.viewport.Viewport
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.Pixmap




class Kamchatka : Game() {
    lateinit var batch: SpriteBatch
    lateinit var worldmap: Texture
    lateinit var camera: OrthographicCamera
    lateinit var viewport: Viewport
    lateinit var debug_font: BitmapFont
    lateinit var debug_batch: SpriteBatch

    
    override fun create() {
        debug_batch = SpriteBatch()
        debug_font = BitmapFont();
        batch = SpriteBatch()
        worldmap = Texture("Sudamerica.png")
        camera = OrthographicCamera()
        viewport = FitViewport(
            worldmap.width.toFloat(),
            worldmap.height.toFloat(),
            camera
        )
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

        worldmap.textureData.prepare()
        val x = Gdx.input.x
        val y = Gdx.input.y

        val color = Color(worldmap.textureData.consumePixmap().getPixel(x,  y))
        println( "col: ${color.toString()}  x: ${x}, y: ${y}")
/*
        debug_batch.begin()
        debug_font.draw(
                debug_batch,
                "x: ${x}, y: ${y}  col: ${color.toString()}",
                Gdx.graphics.width.toFloat()/2,
                Gdx.graphics.height.toFloat()/2
        );
        debug_batch.end()
*/
    }

    override fun dispose() {
        worldmap.dispose()
        batch.dispose()
        debug_batch.dispose()
        debug_font.dispose()
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
    }
}

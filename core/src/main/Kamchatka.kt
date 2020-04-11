//import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.utils.viewport.Viewport
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.ScreenViewport


class Kamchatka : Game() {
    lateinit var batch: SpriteBatch
    lateinit var worldmap: Texture
    lateinit var camera: OrthographicCamera
    lateinit var viewport: Viewport

    
    override fun create() {
        batch = SpriteBatch()
        worldmap = Texture("Sudamerica.png")
        camera = OrthographicCamera()
        viewport = FitViewport(
            worldmap.getWidth().toFloat(),
            worldmap.getHeight().toFloat(),
            camera
        )
    }


    
    override fun render() {

        Gdx.gl.glClearColor(1f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        batch.setProjectionMatrix(camera.combined)

        
        batch.begin()
        batch.draw(worldmap,
                   -worldmap.getWidth().toFloat()/2,
                   -worldmap.getHeight().toFloat()/2
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
}

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class GetGame : ApplicationAdapter() {
    lateinit var batch: SpriteBatch
    lateinit var img: Texture
    
    override fun create() {
        batch = SpriteBatch()
        img = Texture("Sudamerica.png")
    }

    fun imgFitsWidth(width: Float, heigh: Float) : Boolean {
        return (img.getHeight().toFloat()/img.getWidth().toFloat()) >= (heigh/width)
    }

    
    fun computeImageStretch() : Pair<Float, Float> {
        val winWidth = Gdx.graphics.getWidth().toFloat()
        val winHeight = Gdx.graphics.getHeight().toFloat()

        val w = winWidth / img.getWidth()
        val h = winHeight / img.getHeight()

        var pW = 1f
        var pH = 1f
        

        if (imgFitsWidth(winWidth, winHeight)) {
            pW = h/w
            
        } else {
            pH = w/h 
        }

        return Pair(pW * img.getWidth(), pH * img.getHeight())
    }



    
    override fun render() {
        Gdx.gl.glClearColor(1f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        val stretch = computeImageStretch()

        batch.begin()
        batch.draw(img, 0f, 0f,
                   stretch.first, stretch.second, 0, 0,
                   img.getWidth(), img.getHeight(), false, false)
        batch.end()
    }

    override fun dispose() {
        batch.dispose()
        img.dispose()
    }
}

package screens.running

import Country
import IntRectangle
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.Image
import screens.running.countryImage.CountryImage
import screens.running.countryImage.findCountryRectangles

class WorldmapActor(
    worldmapTexture: Texture, countryColorsTexture: Texture, countryColors: CountryColors
) : Group() {

    var countrySelectionListener: CountrySelectionListener? = null

    init {
        setupWorldmapImage(worldmapTexture)
        setupCountryImages(countryColorsTexture, countryColors)
    }

    private fun setupWorldmapImage(worldmapTexture: Texture) {
        val worldmapImage = Image(worldmapTexture)
        worldmapImage.setPosition(0F, 0F)
        addActor(worldmapImage)
    }

    private fun setupCountryImages(
        countryColorsTexture: Texture, countryColors: CountryColors
    ) {
        val countryColorsPixmap = loadCountryColorsPixmap(countryColorsTexture)
        val rectangles = findCountryRectangles(countryColorsPixmap, countryColors)
        rectangles.forEach { (country, rectangle) ->
            val countryColor = countryColors.getValue(country)
            setupCountryImage(country, countryColor, rectangle, countryColorsPixmap)
        }
    }

    private fun loadCountryColorsPixmap(countryColorsTexture: Texture): Pixmap {
        val textureData = countryColorsTexture.textureData
        if (!textureData.isPrepared) {
            textureData.prepare()
        }
        return textureData.consumePixmap()
    }

    private fun setupCountryImage(
        country: Country, countryColor: Color,
        rectangle: IntRectangle, countryColorsPixmap: Pixmap
    ) {
        val image = CountryImage.fromPixmapRectangle(
            countryColorsPixmap, rectangle, countryColor)
        image.addListener(object : InputListener() {
            override fun touchDown(
                event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int
            ) = true

            override fun touchUp(
                event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int
            ) {
                countrySelectionListener?.onCountrySelected(country)
            }

            override fun mouseMoved(event: InputEvent?, x: Float, y: Float): Boolean {
                image.highlight()
                countrySelectionListener?.onCountryMouseOver(country)
                return false
            }

            override fun exit(
                event: InputEvent?, x: Float, y: Float, pointer: Int, toActor: Actor?
            ) {
                if (image.hit(x, y, false) == null) {
                    image.removeHighlight()
                    countrySelectionListener?.onCountryExit(country)
                }
                super.exit(event, x, y, pointer, toActor)
            }
        })
        addActor(image)
    }
}

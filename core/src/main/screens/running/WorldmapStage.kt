package screens.running

import Country
import IntRectangle
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.viewport.Viewport
import screens.running.countryImage.CountryImage
import screens.running.countryImage.findCountryRectangles

class WorldmapStage(
    viewport: Viewport,
    assetManager: AssetManager,
    worldmapTexture: Texture,
    countryColors: CountryColors
) : Stage(viewport) {
    private val countryLabel = Label("", Label.LabelStyle(BitmapFont(), Color.WHITE))
    private var currentCountry: String? = null
        set(value) {
            countryLabel.setText(value ?: "")
            field = value
        }
    var countrySelectionListener: CountrySelectionListener = NoCountrySelectionListener()

    init {
        setupWorldmapImage(worldmapTexture)
        setupCountryImages(assetManager, countryColors)
        setupCountryLabel()
    }

    private fun setupWorldmapImage(worldmapTexture: Texture) {
        val worldmapImage = Image(worldmapTexture)
        worldmapImage.setPosition(0F, 0F)
        addActor(worldmapImage)
    }

    private fun setupCountryImages(
        assetManager: AssetManager, countryColors: CountryColors
    ) {
        val countryColorsPixmap = loadCountryColorsPixmap(assetManager)
        val rectangles = findCountryRectangles(countryColorsPixmap, countryColors)
        rectangles.forEach { (country, rectangle) ->
            val countryColor = countryColors.getValue(country)
            setupCountryImage(country, countryColor, rectangle, countryColorsPixmap)
        }
    }

    private fun loadCountryColorsPixmap(assetManager: AssetManager): Pixmap {
        assetManager.load(COUNTRY_COLORS_FILE, Texture::class.java)
        val countryColorsTexture =
            assetManager.finishLoadingAsset<Texture>(COUNTRY_COLORS_FILE)
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
            override fun touchUp(
                event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int
            ) = countrySelectionListener.onCountrySelected(country)

            override fun mouseMoved(event: InputEvent?, x: Float, y: Float): Boolean {
                image.highlight()
                currentCountry = country
                return false
            }

            override fun exit(
                event: InputEvent?, x: Float, y: Float, pointer: Int, toActor: Actor?
            ) {
                image.removeHighlight()
                currentCountry = null
                super.exit(event, x, y, pointer, toActor)
            }
        })
        addActor(image)
    }

    private fun setupCountryLabel() {
        countryLabel.setFontScale(4F)
        countryLabel.setPosition(8F, 20F)
        addActor(countryLabel)
    }

    companion object {
        private const val COUNTRY_COLORS_FILE = "colores-paises.png"
    }
}

package screens.running

import Country
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.viewport.Viewport
import gamelogic.occupations.CountryOccupations

class RunningStage(
    viewport: Viewport,
    assetManager: AssetManager,
    worldmapTexture: Texture,
    countryColors: CountryColors,
    private val occupations: CountryOccupations
) : Stage(viewport), CountrySelectionListener {
    private val countryLabel = Label("", Label.LabelStyle(BitmapFont(), Color.WHITE))
    private var currentCountry: String? = null
        set(value) {
            if (value != null) {
                countryLabel.setText("""
                    $value
                    Occupied by ${
                        occupations.occupierOf(value)
                    } with ${occupations.armiesOf(value)} armies.                    
                """.trimIndent())
            } else {
                countryLabel.setText("")
            }
            field = value
        }

    init {
        assetManager.load(COUNTRY_COLORS_FILE, Texture::class.java)
        val countryColorsTexture =
            assetManager.finishLoadingAsset<Texture>(COUNTRY_COLORS_FILE)
        val worldmapActor =
            WorldmapActor(worldmapTexture, countryColorsTexture, countryColors)
        worldmapActor.countrySelectionListener = this
        addActor(worldmapActor)
        setupCountryLabel()
        setupNextStateButton()
    }

    private fun setupCountryLabel() {
        countryLabel.setFontScale(4F)
        countryLabel.setPosition(8F, 70F)
        addActor(countryLabel)
    }

    private fun setupNextStateButton() {
        val skin = Skin(Gdx.files.internal("skin/uiskin.json"))
        val textButton = TextButton("Next state", skin)
        textButton.label.setFontScale(2F)
        textButton.width = 4 * textButton.width
        textButton.height = 4 * textButton.height
        textButton.setPosition(width - textButton.width - 8F, 8F)
        addActor(textButton)
    }

    override fun onCountrySelected(country: Country) {

    }

    override fun onCountryMouseOver(country: Country) {
        currentCountry = country
    }

    override fun onCountryExit(country: Country) {
        currentCountry = null
    }

    companion object {
        private const val COUNTRY_COLORS_FILE = "colores-paises.png"
    }
}

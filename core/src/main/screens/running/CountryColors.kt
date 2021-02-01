package screens.running

import Country
import com.badlogic.gdx.graphics.Color

class CountryColors(
    private val countryToColor: Map<Country, Color>
) : Map<Country, Color> by countryToColor {
    private val colorToCountry: Map<Color, Country> =
        countryToColor.entries.associate { (country, color) -> color to country }

    operator fun get(color: Color) = colorToCountry.get(color)
    operator fun contains(color: Color) = colorToCountry.contains(color)
    fun getValue(color: Color) = colorToCountry.getValue(color)
}

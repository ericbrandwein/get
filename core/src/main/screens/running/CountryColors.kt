package screens.running

import Country
import com.badlogic.gdx.graphics.Color

class CountryColors(
    private val countryToColor: Map<Country, Color>
) : Map<Country, Color> by countryToColor {

    operator fun get(color: Color): String? {
        return countryToColor.filterValues { it == color }.keys.firstOrNull()
    }

    operator fun contains(color: Color): Boolean {
        return countryToColor.any { (_, value) -> value == color }
    }
}

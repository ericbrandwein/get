package screens.running

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.JsonReader
import com.badlogic.gdx.utils.JsonValue
import java.io.File

class CountryColors(
    private val countryToColor: Map<String, Color>
) : Map<String, Color> by countryToColor {

    operator fun get(color: Color): String? {
        return countryToColor.filterValues { it == color }.keys.firstOrNull()
    }

    operator fun contains(color: Color): Boolean {
        return countryToColor.any { (_, value) -> value == color }
    }

    companion object {
        fun fromJsonFile(filePath: String): CountryColors {
            return fromJson(File(filePath).readText())
        }

        fun fromJson(json: String): CountryColors {
            val jsonValue: JsonValue = JsonReader().parse(json)
            val map = jsonValue.associateBy(
                { it.name },
                { Color.valueOf(it.asString()) }
            )
            return CountryColors(map)
        }
    }
}

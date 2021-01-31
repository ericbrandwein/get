package screens.running

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.JsonReader
import com.badlogic.gdx.utils.JsonValue
import gamelogic.map.Continent
import gamelogic.map.PoliticalMap
import java.io.File


private const val CONTINENTS_FIELD = "continents"
private const val BORDERS_FIELD = "borders"

fun parseMapInfoFromJsonFile(filePath: String) : Pair<CountryColors, PoliticalMap> =
    parseMapInfoFromJsonString(File(filePath).readText())

fun parseMapInfoFromJsonString(json: String): Pair<CountryColors, PoliticalMap> {
    val jsonValue: JsonValue = JsonReader().parse(json)
    val mapColors = buildMapColorsFromJsonValue(jsonValue)
    val politicalMap = buildPoliticalMapFromJsonValue(jsonValue)

    return Pair(mapColors, politicalMap)
}

private fun buildMapColorsFromJsonValue(jsonValue: JsonValue): CountryColors {
    val mapColorsValues = jsonValue[CONTINENTS_FIELD].flatten().map { country ->
        val color = Color.valueOf(country.asString())
        country.name to color
    }.toMap()
    return CountryColors(mapColorsValues)
}

private fun buildPoliticalMapFromJsonValue(jsonValue: JsonValue): PoliticalMap {
    val builder = PoliticalMap.Builder()
    loadContinentsFromJsonValue(jsonValue, builder)
    loadBordersFromJsonValue(jsonValue, builder)
    return builder.build()
}

private fun loadContinentsFromJsonValue(
    jsonValue: JsonValue,
    builder: PoliticalMap.Builder
) {
    jsonValue[CONTINENTS_FIELD].forEach { continentJsonValue ->
        val countries = continentJsonValue.map { it.name }.toSet()
        val continent = Continent(continentJsonValue.name, countries)
        builder.addContinent(continent)
    }
}

private fun loadBordersFromJsonValue(
    jsonValue: JsonValue,
    builder: PoliticalMap.Builder
) {
    jsonValue[BORDERS_FIELD].forEach { countriesBordering ->
        builder.addBorder(
            countriesBordering[0].asString(),
            countriesBordering[1].asString()
        )
    }
}

package screens.running

import com.badlogic.gdx.graphics.Color
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MapInformationTest {
    private val NO_COUNTRIES_JSON = """
        {
            "continents": {},
            "borders": []
        }
    """.trimIndent()

    @Test
    fun `fromJson with a JSON with no countries should return an empty MapColors`() {
        val (countryColors, politicalMap) = parseMapInfoFromJsonString(NO_COUNTRIES_JSON)

        assertTrue(countryColors.isEmpty())
    }

    @Test
    fun `fromJson with a JSON with one continent and one country should return a MapColors with only that country`() {
        val country = "Argentina"
        val colorString = "#112233"
        val (countryColors, politicalMap) = parseMapInfoFromJsonString(
            """
            {
                "continents": {
                    "América": {
                        "$country": "$colorString"
                    }
                },
                "borders": []
            }
            """.trimMargin()
        )

        assertEquals(1, countryColors.size)
        assertTrue(country in countryColors)
        assertEquals(Color.valueOf(colorString), countryColors[country])
    }

    @Test
    fun `fromJson with a JSON with many continents and many countries should return a corresponding MapColors`() {
        val (countryColors, politicalMap) = parseMapInfoFromJsonString(
            """
            {
                "continents": {
                    "América": {
                        "Argentina": "#112233",
                        "Brasil": "#223344"
                    },
                    "África": {
                        "Sahara": "#665544"
                    }
                },
                "borders": []
            }
            """.trimMargin()
        )

        assertEquals(3, countryColors.size)
    }

    @Test
    fun `fromJson with no countries should load no continents`() {
        val (countryColors, politicalMap) = parseMapInfoFromJsonString(NO_COUNTRIES_JSON)

        assertTrue(politicalMap.countries.isEmpty())
        assertTrue(politicalMap.continents.isEmpty())
    }

    @Test
    fun `fromJson with one country should load only that country in the PoliticalMap`() {
        val continent = "América"
        val country = "Argentina"
        val colorString = "#112233"
        val (countryColors, politicalMap) = parseMapInfoFromJsonString("""
            {
                "continents": {
                    "$continent": {
                        "$country": "$colorString"
                    }
                },
                "borders": []
            }
        """.trimIndent())

        assertEquals(1, politicalMap.countries.size)
        assertEquals(continent, politicalMap.continentOf(country).name)
    }

    @Test
    fun `fromJson with continent with two countries should load both countries into the PoliticalMap`() {
        val continent = "América"
        val countries = listOf("Argentina", "Chile")
        val colorStrings = listOf("#112233", "#223344")
        val (countryColors, politicalMap) = parseMapInfoFromJsonString("""
            {
                "continents": {
                    "$continent": {
                        "${countries[0]}": "${colorStrings[0]}",
                        "${countries[1]}": "${colorStrings[1]}"
                    }
                },
                "borders": []
            }
        """.trimIndent())

        assertEquals(2, politicalMap.countries.size)
        assertEquals(continent, politicalMap.continentOf(countries[0]).name)
        assertEquals(continent, politicalMap.continentOf(countries[1]).name)
    }

    @Test
    fun `fromJson with many continents should load all of them into the PoliticalMap`() {
        val continents = listOf("América", "África")
        val (countryColors, politicalMap) = parseMapInfoFromJsonString("""
            {
                "continents": {
                    "${continents[0]}": {
                        "Argentina": "#112233",
                        "Brasil": "#223344"
                    },
                    "${continents[1]}": {
                        "Sahara": "#334455"
                    }
                },
                "borders": []
            }
        """.trimIndent())

        assertEquals(continents.toSet(), politicalMap.continents.map { it.name }.toSet())
    }

    @Test
    fun `fromJson with border between tow countries loads the border into the PoliticalMap`() {
        val countries = listOf("Argentina", "Brasil")
        val (countryColors, politicalMap) = parseMapInfoFromJsonString("""
            {
                "continents": {
                    "America": {
                        "${countries[0]}": "#112233",
                        "${countries[1]}": "#223344"
                    }
                },
                "borders": [
                    ["${countries[0]}", "${countries[1]}"]
                ]
            }
        """.trimIndent())

        assertTrue(politicalMap.areBordering(countries[0], countries[1]))
    }

}

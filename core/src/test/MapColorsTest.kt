import com.badlogic.gdx.graphics.Color
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MapColorsTest {
    @Test
    fun `Empty JSON object should parse to an empty Map`() {
        val colors = MapColors.fromJson("{}")

        assertTrue(colors.isEmpty())
    }

    @Test
    fun `JSON with one country should parse to a Map with that country only`() {
        val name = "Argentina"
        val colorString = "#aabbcc"
        val color = Color.valueOf(colorString)
        val colors = MapColors.fromJson(
            """
                {
                    "$name": "$colorString"
                }
            """.trimIndent()
        )

        assertEquals(1, colors.size)
        assertTrue(colors.containsKey(name))
        assertEquals(color, colors[name])
        assertEquals(name, colors[color])
    }

    @Test
    fun `JSON with many countries should parse to a Map with that countries only`() {
        val names = listOf("Argentina", "Brasil")
        val colorStrings = listOf("#aabbcc", "#ddeeff")
        val colors = colorStrings.map { Color.valueOf(it) }
        val result = MapColors.fromJson(
            """
                {
                    "${names[0]}": "${colorStrings[0]}",
                    "${names[1]}": "${colorStrings[1]}"
                }
            """.trimIndent()
        )

        assertEquals(2, result.size)
        names.forEachIndexed { index, name ->
            assertTrue(result.containsKey(name))
            assertEquals(colors[index], result[name])
            assertEquals(name, result[colors[index]])
        }
    }
}

import kotlin.test.*

class RefereeTest {
    @Test
    fun `Can't create from a negative Int`() {
        val negative = -3

        val exception = assertFailsWith<NonPositiveNumberException> {
            PositiveInt(negative)
        }

        assertEquals(negative, exception.number)
    }

}
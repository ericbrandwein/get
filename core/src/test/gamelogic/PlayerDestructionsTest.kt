package gamelogic

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PlayerDestructionsTest {
    @Test
    fun `No player isDestroyed in the beginning`() {
        val destructions = PlayerDestructions()

        assertFalse(destructions.isDestroyed("Eric"))
    }

    @Test
    fun `Destroyed player isDestroyed`() {
        val destructions = PlayerDestructions()

        val destroyed = "Eric"
        val destroyer = "Nico"
        destructions.destroy(destroyed, destroyer)

        assertTrue(destructions.isDestroyed(destroyed))
        assertEquals(destroyer, destructions.destroyerOf(destroyed))
    }

    @Test
    fun `Destroying one player doesn't destroy all others`() {
        val destructions = PlayerDestructions()

        destructions.destroy("Eric", "Nico")

        assertFalse(destructions.isDestroyed("Nico"))
    }

    @Test
    fun `Can destroy more than one player`() {
        val destructions = PlayerDestructions()

        val playersDestroyed = listOf("Eric", "Cristel")
        val destroyer = "Nico"
        playersDestroyed.forEach { destructions.destroy(it, destroyer) }

        playersDestroyed.forEach {
            assertTrue(destructions.isDestroyed(it))
            assertEquals(destroyer, destructions.destroyerOf(it))
        }
    }

    @Test
    fun `destroyerOf players destroyed by different destroyed return the corresponding destroyers`() {
        val destructions = PlayerDestructions()

        val playersDestroyed = listOf("Eric", "Cristel")
        val destroyers = listOf("Nico", "Eric")
        val destroyedWithDestroyers = playersDestroyed.zip(destroyers)
        destroyedWithDestroyers.forEach { (destroyed, destroyer) ->
            destructions.destroy(destroyed, destroyer)
        }

        destroyedWithDestroyers.forEach { (destroyed, destroyer) ->
            assertTrue(destructions.isDestroyed(destroyed))
            assertEquals(destroyer, destructions.destroyerOf(destroyed))
        }
    }

    @Test
    fun `Can't destroy the same player twice`() {
        val destructions = PlayerDestructions()

        val destroyed = "Eric"
        val destroyer = "Nico"
        destructions.destroy(destroyed, destroyer)

        val exception = assertFailsWith<PlayerAlreadyDestroyedException> {
            destructions.destroy(destroyed, "Cristel")
        }

        assertEquals(destroyed, exception.player)
        assertTrue(destructions.isDestroyed(destroyed))
        assertEquals(destroyer, destructions.destroyerOf(destroyed))
    }
}


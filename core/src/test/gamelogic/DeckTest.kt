package gamelogic

import kotlin.random.Random
import kotlin.test.*

class DeckTest {
    @Test
    fun `Taking a card from the deck should return a shuffled card`() {
        val cards = listOf("hola", "como", "estas")
        val deck = Deck(cards)

        assertEquals(3, deck.size)
        assertTrue(deck.takeCard() in cards)
        assertEquals(2, deck.size)
    }

    @Test
    fun `Taking two cards from the deck should return two shuffled cards`() {
        val cards = listOf("hola", "como", "estas")
        val deck = Deck(cards)

        assertEquals(3, deck.size)
        val firstCard = deck.takeCard()
        assertEquals(2, deck.size)
        assertTrue(firstCard in cards)
        val secondCard = deck.takeCard()
        assertEquals(1, deck.size)
        assertTrue(secondCard in cards)
        assertNotEquals(secondCard, firstCard)
    }

    @Test
    fun `Can't take more cards than the originals without adding more`() {
        val cards = listOf("hola", "como", "estas")
        val deck = Deck(cards)

        repeat(cards.size) {
            deck.takeCard()
        }
        assertFailsWith(NoSuchElementException::class) {
            deck.takeCard()
        }
        assertEquals(0, deck.size)
        assertTrue(deck.isEmpty())
    }

    @Test
    fun `Can remove card from deck after adding it`() {
        val deck = Deck<String>()

        assertTrue(deck.isEmpty())
        val card = "a card"
        deck.add(card)
        assertEquals(1, deck.size)
        assertEquals(deck.takeCard(), card)
        assertEquals(0, deck.size)
    }

    @Test
    fun `Can provide a random number generator for shuffling`() {
        val deck = Deck(cards = listOf("hola", "como", "estas"), shuffler = Random(1234))

        assertEquals("hola", deck.takeCard())
        assertEquals("estas", deck.takeCard())
        assertEquals("como", deck.takeCard())
    }

    @Test
    fun `Can't remove added cards until after original cards are removed`() {
        val deck = Deck(cards = listOf("hola", "como", "estas"), shuffler = Random(1234))
        deck.add("nuevo")

        assertEquals("hola", deck.takeCard())
        assertEquals("estas", deck.takeCard())
        assertEquals("como", deck.takeCard())
        assertEquals("nuevo", deck.takeCard())
    }

    @Test
    fun `Added cards are shuffled`() {
        val deck = Deck(cards = listOf("hola", "como", "estas"), shuffler = Random(12))
        deck.add("nuevo")
        deck.add("otro nuevo")

        repeat(3) {
            deck.takeCard()
        }
        assertEquals("otro nuevo", deck.takeCard())
        assertEquals("nuevo", deck.takeCard())
    }

    @Test
    fun `isEmpty returns false after adding new cards`() {
        val deck = Deck<String>()
        deck.add("hola")

        assertFalse(deck.isEmpty())
    }

    @Test
    fun `isEmpty returns false when starting deck with cards`() {
        val deck = Deck(listOf("hola"))

        assertFalse(deck.isEmpty())
    }

    @Test
    fun `isEmpty returns true when deck has no cards`() {
        val deck = Deck<String>()

        assertTrue(deck.isEmpty())
    }

}


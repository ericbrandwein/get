package gamelogic

import kotlin.random.Random

class Deck<T> {

    private var cards: MutableCollection<T>
    private val discardPile = mutableListOf<T>()
    private val shuffler: Random
    val size: Int get() = cards.size + discardPile.size

    constructor(cards: Collection<T>, shuffler: Random) {
        this.shuffler = shuffler
        this.cards = mutableShuffled(cards)
    }

    constructor(cards: Collection<T>) : this(cards, Random.Default)
    constructor() : this(emptyList())

    fun takeCard(): T {
        if (cards.isEmpty()) { shuffleDiscardPile() }
        return pop()
    }

    private fun shuffleDiscardPile() {
        cards = mutableShuffled(discardPile)
        discardPile.clear()
    }

    private fun pop(): T {
        val card = cards.first()
        cards.remove(card)
        return card
    }

    private fun mutableShuffled(original: Collection<T>): MutableCollection<T> {
        return ArrayList(original.shuffled(shuffler))
    }

    fun isEmpty() = size == 0
    fun add(card: T) = discardPile.add(card)
}

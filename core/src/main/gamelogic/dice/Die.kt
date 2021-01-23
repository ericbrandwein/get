package gamelogic.dice

/**
 * A 6-sided die.
 */
interface Die {
    fun roll(): Int
    fun roll(times: Int): Collection<Int> = List(times) { roll() }
}

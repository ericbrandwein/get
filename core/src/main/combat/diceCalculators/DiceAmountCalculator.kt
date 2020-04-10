package combat.diceCalculators

import PositiveInt

interface DiceAmountCalculator {
    fun forAttacker(): PositiveInt
    fun forDefender(): PositiveInt
}

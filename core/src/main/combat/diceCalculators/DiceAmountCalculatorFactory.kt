package combat.diceCalculators

import PositiveInt

interface DiceAmountCalculatorFactory {
    fun forAttack(
        attackingArmies: PositiveInt, defendingArmies: PositiveInt
    ): DiceAmountCalculator
}

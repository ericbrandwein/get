package combat.diceCalculators

import PositiveInt

class ClassicCombatDiceAmountCalculatorFactory : DiceAmountCalculatorFactory {
    override fun forAttack(attackingArmies: PositiveInt, defendingArmies: PositiveInt) =
        ClassicCombatDiceAmountCalculator(attackingArmies, defendingArmies)
}

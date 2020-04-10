package combat

import combat.diceCalculators.DiceAmountCalculator

class NotEnoughArmiesForAttackException : IllegalArgumentException(
    "Can't attack with less than ${DiceAmountCalculator.ATTACKER_MINIMUM_ARMIES} armies.")

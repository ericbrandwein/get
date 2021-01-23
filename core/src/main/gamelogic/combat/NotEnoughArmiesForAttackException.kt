package gamelogic.combat

class NotEnoughArmiesForAttackException : IllegalArgumentException(
    "Can't attack with less than ${DiceAmountCalculator.ATTACKER_MINIMUM_ARMIES} armies.")

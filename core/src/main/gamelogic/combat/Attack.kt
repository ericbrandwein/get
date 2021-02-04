package gamelogic.combat

import PositiveInt
import gamelogic.combat.resolver.CombatResolver

/**
 * Represents an attack between two [AttackOccupations]. Can be applied to them by
 * calling [apply]. If the attack [isConquering], the amount of armies to move from one
 * country to the other should be passed to [apply].
 *
 * @see Attacker
 */
class Attack(
    private val attackOccupations: AttackOccupations, combatResolver: CombatResolver
) {
    val isConquering: Boolean
    val armiesLostByDefender: Int
    val armiesLostByAttacker: Int
    val attackerRolls: Collection<Int>
    val defenderRolls: Collection<Int>
    val attackerRemainingArmies: PositiveInt

    init {
        val combatResults = combatResolver.combat(
            attackOccupations.armiesOfAttacker(), attackOccupations.armiesOfDefender()
        )
        isConquering =
            combatResults.armiesLostByDefender ==
                attackOccupations.armiesOfDefender().toInt()
        armiesLostByDefender = combatResults.armiesLostByDefender
        armiesLostByAttacker = combatResults.armiesLostByAttacker
        attackerRolls = combatResults.attackerRolls
        defenderRolls = combatResults.defenderRolls
        attackerRemainingArmies = attackOccupations.armiesOfAttacker() - armiesLostByAttacker
    }

    fun apply(armiesToMove: PositiveInt) {
        assertIsConquering()
        assertCanConquerWithArmies(armiesToMove)
        val armiesRemovedFromAttacker = (armiesToMove + armiesLostByAttacker).toInt()
        attackOccupations.removeArmiesFromAttacker(armiesRemovedFromAttacker)
        attackOccupations.occupyDefendingCountryWithAttackingPlayer(armiesToMove)
    }

    fun apply() {
        assertIsNotConquering()
        attackOccupations.removeArmiesFromAttacker(armiesLostByAttacker)
        attackOccupations.removeArmiesFromDefender(armiesLostByDefender)
    }

    private fun assertIsConquering() {
        if (!isConquering) {
            throw ArmiesProvidedWhenNotOccupyingException()
        }
    }

    private fun assertIsNotConquering() {
        if (isConquering) {
            throw NoArmiesProvidedWhenOccupyingException()
        }
    }

    private fun assertCanConquerWithArmies(armiesMoved: PositiveInt) {
        if (armiesMoved > MAX_CONQUERING_ARMIES) {
            throw TooManyArmiesMovedException(armiesMoved)
        }
    }

    companion object {
        val MAX_CONQUERING_ARMIES = PositiveInt(3)
    }
}

class NoArmiesProvidedWhenOccupyingException :
    Exception("Can't apply a conquering Attack without providing armies to move.")

class ArmiesProvidedWhenNotOccupyingException :
    Exception("Can't apply a non conquering Attack providing armies to move.")

class TooManyArmiesMovedException(val armies: PositiveInt) : Exception(
    "Can't move $armies armies, it would be more than " +
        "the maximum of ${Attack.MAX_CONQUERING_ARMIES}."
)

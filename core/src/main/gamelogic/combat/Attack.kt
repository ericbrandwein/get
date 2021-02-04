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
class Attack(attackOccupations: AttackOccupations, combatResolver: CombatResolver) {
    val isConquering: Boolean
    val armiesLostByDefender: Int
    val armiesLostByAttacker: Int
    val attackerRolls: Collection<Int>
    val defenderRolls: Collection<Int>
    val attackerRemainingArmies: PositiveInt

    private val applier: AttackApplier

    init {
        val combatResults = combatResolver.combat(
            attackOccupations.armiesOfAttacker(), attackOccupations.armiesOfDefender()
        )
        armiesLostByDefender = combatResults.armiesLostByDefender
        armiesLostByAttacker = combatResults.armiesLostByAttacker
        attackerRolls = combatResults.attackerRolls
        defenderRolls = combatResults.defenderRolls
        attackerRemainingArmies =
            attackOccupations.armiesOfAttacker() - armiesLostByAttacker
        applier = AttackApplier.forAttack(
            attackOccupations, armiesLostByAttacker, armiesLostByDefender)
        isConquering = applier.isConquering
    }

    fun apply() = applier.apply()
    fun apply(armiesToMove: PositiveInt) = applier.apply(armiesToMove)

    companion object {
        val MAX_CONQUERING_ARMIES = PositiveInt(3)
    }
}

private interface AttackApplier {
    val isConquering: Boolean
    fun apply()
    fun apply(armiesToMove: PositiveInt)

    companion object {
        fun forAttack(
            occupations: AttackOccupations,
            armiesLostByAttacker: Int,
            armiesLostByDefender: Int
        ): AttackApplier {
            val isConquering =
                armiesLostByDefender == occupations.armiesOfDefender().toInt()
            return if (isConquering) {
                ConqueringAttackApplier(occupations, armiesLostByAttacker)
            } else {
                NotConqueringAttackApplier(
                    occupations, armiesLostByAttacker, armiesLostByDefender)
            }
        }
    }
}

private class NotConqueringAttackApplier(
    private val occupations: AttackOccupations,
    private val armiesLostByAttacker: Int,
    private val armiesLostByDefender: Int
) : AttackApplier {
    override val isConquering = false

    override fun apply() {
        occupations.removeArmiesFromAttacker(armiesLostByAttacker)
        occupations.removeArmiesFromDefender(armiesLostByDefender)
    }

    override fun apply(armiesToMove: PositiveInt) =
        throw ArmiesProvidedWhenNotOccupyingException()
}

private class ConqueringAttackApplier(
    private val occupations: AttackOccupations,
    private val armiesLostByAttacker: Int
) : AttackApplier {
    override val isConquering = true

    override fun apply() = throw NoArmiesProvidedWhenOccupyingException()

    override fun apply(armiesToMove: PositiveInt) {
        assertCanConquerWithArmies(armiesToMove)
        val armiesRemovedFromAttacker = (armiesToMove + armiesLostByAttacker).toInt()
        occupations.removeArmiesFromAttacker(armiesRemovedFromAttacker)
        occupations.occupyDefendingCountryWithAttackingPlayer(armiesToMove)
    }

    private fun assertCanConquerWithArmies(armiesMoved: PositiveInt) {
        if (armiesMoved > Attack.MAX_CONQUERING_ARMIES) {
            throw TooManyArmiesMovedException(armiesMoved)
        }
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

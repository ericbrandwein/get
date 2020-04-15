package combat

import PositiveInt
import combat.resolver.CombatResolver

class Attack(
    private val attackOccupations: AttackOccupations,
    private val combatResolver: CombatResolver,
    private val conqueror: Conqueror
) {
    fun run(): CombatResults {
        val attackerArmies = armiesOfAttacker()
        val defenderArmies = armiesOfDefender()
        val combatResults = combatResolver.combat(attackerArmies, defenderArmies)
        applyCombatResults(combatResults)
        return combatResults
    }

    private fun armiesOfAttacker() = attackOccupations.armiesOfAttacker()
    private fun armiesOfDefender() = attackOccupations.armiesOfDefender()

    private fun applyCombatResults(results: CombatResults) {
        val armiesLostByAttacker = results.armiesLostByAttacker
        removeArmiesFromAttacker(armiesLostByAttacker)
        val armiesLostByDefender = results.armiesLostByDefender
        val defenderArmies = armiesOfDefender().toInt()
        if (armiesLostByDefender == defenderArmies) {
            conquer()
        } else {
            removeArmiesFromDefender(armiesLostByDefender)
        }
    }

    private fun conquer() {
        val armiesMoved = getArmiesToConquerWith()
        removeArmiesFromAttacker(armiesMoved.toInt())
        val attackingPlayer = attackOccupations.attackingPlayer()
        attackOccupations.occupyDefendingCountry(attackingPlayer, armiesMoved)
    }

    private fun getArmiesToConquerWith(): PositiveInt {
        val armiesMoved = conqueror.armiesToMove(armiesOfAttacker())
        assertCanConquerWithArmies(armiesMoved)
        return armiesMoved
    }

    private fun assertCanConquerWithArmies(armiesMoved: PositiveInt) {
        if (armiesMoved > MAX_CONQUERING_ARMIES) {
            throw TooManyArmiesMovedException(armiesMoved)
        }
    }

    private fun removeArmiesFromAttacker(armies: Int) =
        attackOccupations.removeArmiesFromAttacker(armies)

    private fun removeArmiesFromDefender(armies: Int) =
        attackOccupations.removeArmiesFromDefender(armies)

    companion object {
        val MAX_CONQUERING_ARMIES = PositiveInt(3)
    }
}

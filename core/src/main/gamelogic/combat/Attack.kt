package gamelogic.combat

import PositiveInt
import gamelogic.combat.resolver.CombatResolver

class Attack(
    private val attackOccupations: AttackOccupations,
    private val combatResolver: CombatResolver,
    private val conqueror: Conqueror
) {
    fun run(): CombatResults {
        val attackerArmies = attackOccupations.armiesOfAttacker()
        val defenderArmies = attackOccupations.armiesOfDefender()
        val combatResults = combatResolver.combat(attackerArmies, defenderArmies)
        applyCombatResults(combatResults)
        return combatResults
    }

    private fun applyCombatResults(results: CombatResults) {
        val armiesLostByAttacker = results.armiesLostByAttacker
        attackOccupations.removeArmiesFromAttacker(armiesLostByAttacker)
        val armiesLostByDefender = results.armiesLostByDefender
        val defenderArmies = attackOccupations.armiesOfDefender().toInt()
        if (armiesLostByDefender == defenderArmies) {
            conquer()
        } else {
            attackOccupations.removeArmiesFromDefender(armiesLostByDefender)
        }
    }

    private fun conquer() {
        val armiesMoved = getArmiesToConquerWith()
        attackOccupations.removeArmiesFromAttacker(armiesMoved.toInt())
        attackOccupations.occupyDefendingCountryWithAttackingPlayer(armiesMoved)
    }

    private fun getArmiesToConquerWith(): PositiveInt {
        val armiesMoved = conqueror.armiesToMove(attackOccupations.armiesOfAttacker())
        assertCanConquerWithArmies(armiesMoved)
        return armiesMoved
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

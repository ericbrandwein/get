package gamelogic.combat

data class CombatResults(
    private val lostArmies: Pair<Int, Int>,
    private val rolls: Pair<Collection<Int>, Collection<Int>>
) {
    val armiesLostByAttacker = lostArmies.first
    val armiesLostByDefender = lostArmies.second
    val attackerRolls: Collection<Int> = rolls.first
    val defenderRolls: Collection<Int> = rolls.second
}

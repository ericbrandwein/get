package gamelogic

import gamelogic.map.PoliticalMap
import gamelogic.occupations.CountryOccupations
import Country
import Player
import PositiveInt
import gamelogic.combat.Attacker
import gamelogic.combat.Conqueror
import gamelogic.combat.resolver.CombatDiceRoller
import gamelogic.combat.resolver.DiceRollingCombatResolver
import gamelogic.dice.RandomDie
import gamelogic.situations.classicCombat.ClassicCombatDiceAmountCalculator


//TODO: En lugar de crear esta clase lo que hay que hacer es sacar del ataquer la
//      responsabilidad de mover las fichas después de atacar. Peeero, no solo hay
//      que hacer refactor del package combat, sino también de los tests!!! Asi que
//      queda para otro backlog item.
class SkipRegroup : Conqueror {
    override fun armiesToMove(remainingAttackingArmies: PositiveInt): PositiveInt {
        return PositiveInt(0)
    }

}

class CountryReinforcement(val country:Country, val n: PositiveInt) {
    public fun apply(player: Player,occupations: CountryOccupations) {
        if (occupations.occupierOf(country) != player) {
            throw java.lang.Exception("Player ${player} cannot add army to country")
        }
       occupations.addArmies(country, n)
    }
}

/**
 * preconds (checkqed in validateRegroupings method):
 *      1. regroupings.distinctBy { it.from }.count() == regroupings.count()
 *      2. occupier of from and to is the same and the currentPlayer
 */
class Regrouping(val from:Country, val to:Country, val n: PositiveInt, val referee: Referee) {
    init {
        if (n.toInt() > 3) { throw Exception("At most 3 armies can be regrouped") }
        if (referee.occupations.armiesOf(from) <= n) {
            throw Exception("Cannot move ${n.toInt()} countries if they are not available in country")
        }
        if (!referee.politicalMap.areBordering(from, to)) {
            throw Exception("countries must be bordering to regroup nut ${from} and ${to} are not")
        }
    }
    public fun apply() {
        referee.occupations.removeArmies(from, n)
        referee.occupations.addArmies(to, n)
    }
}
/**
 * The Referee of the game
 *
 * @property players the players sorted according to their turns
 * @property politicalMap the board with countries, continents and connexions between countries
 * @property occupations the players' occupations (Starts being the initial setting, it is updated after each attack).
 *
 * Referee has 3 states.
 * AddArmies: when current player adds armies to his countries (this is one action)
 * Attack: when current player attacks any enemy. This action may be repeated
 * Regroup: when current player moves his armies after the Attack phase
 */
class Referee (val players:MutableList<PlayerInfo>, val politicalMap: PoliticalMap, val occupations: CountryOccupations){
    enum class State{AddArmies, Attack, Regroup}
    var player_index = 0
    var state = State.AddArmies

    public fun next(state:State) : State{
        when (state) {
            State.AddArmies -> return State.Attack
            State.Attack -> return State.Regroup
            State.Regroup -> return State.AddArmies
        }
    }

    fun currentState():State { return state }
    private fun toNextState() {
        state = next(state)
        if (state == State.AddArmies) { changeTurn() }
    }
    fun currentPlayer(): Player {  return players[player_index % players.size].name }
    private fun changeTurn(){ ++player_index }

    fun gameIsOver() : Boolean {
        return players.any{ it.reachedTheGoal(this) }
    }

    fun winners() : List<Player> {
        return players.filter{ it.reachedTheGoal(this) }.map{it.name}
    }
    fun addArmies(reinforcements: List<CountryReinforcement>) {
       reinforcements.forEach { it.apply(currentPlayer(), occupations) }
    }

    fun makeAttack(from:Country, to:Country) {
        if (occupations.occupierOf(from) != currentPlayer()){
            throw Exception("Player ${currentPlayer()} does not occupy ${from}")
        }
        val combatResolver = DiceRollingCombatResolver(CombatDiceRoller(ClassicCombatDiceAmountCalculator(), RandomDie()))
        val attacker = Attacker(occupations, combatResolver)
        attacker.attack(from, to, SkipRegroup())
    }
    fun validateRegroupings(regroupings: List<Regrouping>) {

        if(regroupings.any{ occupations.occupierOf(it.from) != currentPlayer() || occupations.occupierOf(it.to) != currentPlayer() }) {
            throw java.lang.Exception("player must occupy both countries to regroup")
        }

        if (regroupings.distinctBy { it.from }.count() != regroupings.count()) {
            throw Exception("Only one regroup per country per turn is allowed (to facilitate validation)")
        }
    }

    fun regroup(regroupings: List<Regrouping>){
        validateRegroupings(regroupings)
        regroupings.map { it.apply() }
    }
}

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

//TODO: Hay que hacer un chequeo de que en el resultado final de la suma de agrupamientos
//      el reagrupamiento es valido en el sentido en que nadie mueve un ejercito mas de una vez
/**
 * precond: regroupings.distinctBy { it.from }.count() == regroupings.count()
 */
class Regrouping(val from:Country, val to:Country, val n: PositiveInt) {
    init {
        if (n.toInt() > 3) {
            throw Exception("At most 3 armes==ies can be regrouped")
        }
    }
    public fun apply(player: Player, politicalMap: PoliticalMap, occupations: CountryOccupations){
       if (occupations.occupierOf(from) != player || occupations.occupierOf(to) != player) {
           throw Exception("player ${player} must occupy countries ${from} and ${to} to reagroup")
       }
       if (!politicalMap.areBordering(from, to)) {
           throw Exception("countries must be bordering to regroup nut ${from} and ${to} are not")
       }
       if (occupations.armiesOf(from) < n.plus(PositiveInt(1)) ) {
           throw Exception("At least one army must remain in each country after regroup, ${from} has to few")
       }

        occupations.removeArmies(from, n)
        occupations.addArmies(to, n)
    }
}
/**
 * The Referee of the game
 *
 * @property players the players sorted according to their turns
 * @property politicalMap the board with countries, continents and connexions between countries
 * @property occupations the players' occupations (Starts being the initial setting, it is updated after each attack).
 */
class Referee (val players:List<PlayerInfo>, val politicalMap: PoliticalMap, val occupations: CountryOccupations){
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
        return players.map{ it.reachedTheGoal(this) }.any()
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

    fun regroup(regroupings: List<Regrouping>){
        if(regroupings.any{ occupations.occupierOf(it.from) != currentPlayer() || occupations.occupierOf(it.to) != currentPlayer() }) {
            throw java.lang.Exception("player must occupy both countrys to regroup")
        }
        if (regroupings.distinctBy { it.from }.count() != regroupings.count()) {
           throw java.lang.Exception ("Only one time we allow a country to regroup per turn, this make easier to validate the move")
        }
        regroupings.map { it.apply(currentPlayer(),politicalMap,occupations) }
    }
}

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
        return PositiveInt(1)
    }

}

class CountryReinforcement(val country:Country, val armies: PositiveInt) {
    public fun apply(player: Player,occupations: CountryOccupations) {
        if (occupations.occupierOf(country) != player) {
            throw Exception("Player ${player} cannot add army to country")
        }
       occupations.addArmies(country, armies)
    }
}

/**
 * preconditions (checked in [validateRegroupings] method):
 *      1. `regroupings.distinctBy { it.from }.count() == regroupings.count()`
 *      2. occupier of from and to is the same as the currentPlayer
 */
class Regrouping(val from:Country, val to:Country, val armies: PositiveInt, val referee: Referee) {
    init {
        if (referee.occupations.armiesOf(from) <= armies) {
            throw Exception("Cannot move ${armies.toInt()} armies if they are not available in country")
        }
        if (!referee.politicalMap.areBordering(from, to)) {
            throw Exception("countries must be bordering to regroup but ${from} and ${to} are not")
        }
    }
    public fun apply() {
        referee.occupations.removeArmies(from, armies)
        referee.occupations.addArmies(to, armies)
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
    enum class State{
        AddArmies {
        override fun next(referee: Referee): State { return Attack }
    }, Attack {
        override fun next(referee: Referee): State { return Regroup }
    }, Regroup {
        override fun next(referee: Referee): State { referee.changeTurn(); return AddArmies }
    }; abstract fun next(referee: Referee):State}
    enum class AttackState{Fight, Occupation}

    private var playerIndex = 0
    private var state = State.AddArmies
    private var attackState = AttackState.Fight
    private var occupiedCountry : Country? = null
    private var attackerCountry : Country? = null

    val currentState:State
        get() = state

    val currentAttackState:AttackState
        get() = attackState

    private fun toNextState() {
        state = state.next(this)
    }
    fun currentPlayer(): Player {  return players[playerIndex % players.size].name }
    private fun changeTurn(){ ++playerIndex }

    val gameIsOver : Boolean
        get() = players.any{ it.reachedTheGoal(this) }


    val winners : List<Player>
        get() = players.filter{ it.reachedTheGoal(this) }.map{it.name}

    fun addArmies(reinforcements: List<CountryReinforcement>) {
        reinforcements.forEach { it.apply(currentPlayer(), occupations) }
        toNextState()
    }

    fun makeAttack(from:Country, to:Country) {
        if (state != State.Attack) { throw Exception("Cannot attack when not in attacking state") }
        else if (attackState != AttackState.Fight) { throw Exception("Cannot attack if not fighting") }
        attackerCountry = from

        if (occupations.occupierOf(from) != currentPlayer()){
            throw Exception("Player ${currentPlayer()} does not occupy ${from}")
        }
        val combatResolver = DiceRollingCombatResolver(CombatDiceRoller(ClassicCombatDiceAmountCalculator(), RandomDie()))
        val attacker = Attacker(occupations, combatResolver)
        attacker.attack(from, to, SkipRegroup())
        if (occupations.occupierOf(to) == currentPlayer()) {
            attackState = AttackState.Occupation
            occupiedCountry = to
        }
    }
    fun occupyConqueredCountry(armies: PositiveInt) {
        if (state != State.Attack || attackState != AttackState.Occupation) {
           throw Exception("Not the moment to occupy conquered country")
        }
        if (attackerCountry == null || occupiedCountry == null) {
           throw Exception("Mmm, no country from|to occupy...")
        }
        if (occupations.armiesOf(attackerCountry!!) <= armies) {
            throw Exception("Not enough countries to occupy the conquered one")
        }
        if (armies > PositiveInt(1)) {
            val armiesToMove = armies - PositiveInt(1)
            occupations.addArmies(occupiedCountry!!, armiesToMove)
            occupations.removeArmies(attackerCountry!!, armiesToMove)
        }
        attackState = AttackState.Fight
        occupiedCountry = null
        attackerCountry = null
    }

    fun endAttack() {
        if (state != State.Attack) { throw Exception("Cannot end attack if not attaking")}
        toNextState()
    }
    fun validateRegroupings(regroupings: List<Regrouping>) {

        if(regroupings.any{ occupations.occupierOf(it.from) != currentPlayer() || occupations.occupierOf(it.to) != currentPlayer() }) {
            throw Exception("player must occupy both countries to regroup")
        }

        if (regroupings.distinctBy { it.from }.count() != regroupings.count()) {
            throw Exception("Only one regroup per country per turn is allowed (to facilitate validation)")
        }
    }

    fun regroup(regroupings: List<Regrouping>){
        if (state != State.Regroup) { throw Exception("Cannot regroup if not regrouping") }
        validateRegroupings(regroupings)
        regroupings.map { it.apply() }
        toNextState()
    }
}

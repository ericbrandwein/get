package gamelogic.combat

import Country
import gamelogic.combat.resolver.CombatResolver
import gamelogic.occupations.CountryOccupations

//TODO: refactor this class
//      The attacker should not make the country occupation after conquer, because
//      first the referee changes to a movement AttackState requesting for the number
//      of armies to move.
//      The main problem here (beside what it seems to me like a very cryptic set of
//      classes that manage all this) is that PositiveInt cannot be zero, so we cannot
//      leave zero armies in a country that a player have just lost.
//      An ugly hack (this is the current implementation) could be to move one army in
//      this state and then in the movement action move n-1 (n the requested number).
//      (Maybe this is not so ugly, anyway the refactor is needed, for instance, to
//      get a cleaner way to figure out the result of the attack, etc.)
//      Another option is to use other type instead of PostitiveInt, but that is a huge
//      refactor and also attempts against the reasons behind the election of that type.
class Attacker(
    private val countryOccupations: CountryOccupations,
    private val combatResolver: CombatResolver
) {
    fun attack(from: Country, to: Country, conqueror: Conqueror): CombatResults {
        val attackingOccupations = AttackOccupations(countryOccupations, from, to)
        return Attack(attackingOccupations, combatResolver, conqueror).run()
    }
}

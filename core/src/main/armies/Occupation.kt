package armies

import Country

/**
 * Defines an occupation of a country by zero or more players.
 * Depending on the occupation situation, one can add or remove armies,
 * take the entire country for another player, or take a share of the country.
 */
interface Occupation {
    val occupiers: Set<Player>
    val country: Country
    val occupier: Player
    val armies: Int

    fun take(player: Player, armies: Int): Occupation =
        SinglePlayerOccupation(country, player, armies)

    fun take(
        firstPlayer: Player, secondPlayer: Player, firstArmies: Int, secondArmies: Int
    ): Occupation =
        SharedOccupation(country, firstPlayer, secondPlayer, firstArmies, secondArmies)

    fun addArmies(armies: Int)
    fun addArmies(armies: Int, player: Player)
    fun removeArmies(armies: Int)
    fun armiesFor(player: Player): Int
    fun removeArmies(armies: Int, player: Player)
    fun takeShare(currentPlayer: Player, newPlayer: Player, armies: Int): Occupation
    fun isShared() = false
}

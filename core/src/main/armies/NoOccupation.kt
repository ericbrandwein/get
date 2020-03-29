package armies

import Country

class NoOccupation(override val country: Country) : Occupation {
    override val occupier get() = throw UnoccupiedCountryException(country)
    override val occupiers = setOf<Player>()
    override val armies = 0

    override fun addArmies(armies: Int) = throw UnoccupiedCountryException(country)

    override fun addArmies(armies: Int, player: Player) =
        throw PlayerIsNotOccupyingCountryException(country, player)

    override fun removeArmies(armies: Int, player: Player) =
        throw PlayerIsNotOccupyingCountryException(country, player)

    override fun takeShare(currentPlayer: Player, newPlayer: Player, armies: Int) =
        throw CountryNotSharedException(country)

    override fun removeArmies(armies: Int) = throw UnoccupiedCountryException(country)
    override fun armiesFor(player: Player) =
        throw PlayerIsNotOccupyingCountryException(country, player)
}

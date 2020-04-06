package countries

import Country
import Player

data class Occupation(val country: Country, val occupier: Player, val armies: Int)

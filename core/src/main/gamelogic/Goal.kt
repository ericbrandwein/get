package gamelogic

import gamelogic.map.Continent
import java.lang.Exception

abstract class Goal {
    abstract fun achieved(player:PlayerInfo, referee:Referee):Boolean
}


abstract class SubGoal : Goal() {

}

class OccupyContinent(val continent:Continent) : SubGoal() {
    override fun achieved(player: PlayerInfo, referee: Referee): Boolean {
        TODO("Not yet implemented")
    }
}

class OccupySubContinent(val continent:Continent, val countries:Int): SubGoal() {
    init {
        if (continent.countries.size < countries) {
            throw Exception("${continent.name} has less than ${countries} countries")
        }
    }
    override fun achieved(player: PlayerInfo, referee: Referee): Boolean {
        TODO("Not yet implemented")
    }
}

class Destroy(val army:PlayerInfo) : Goal() {
    override fun achieved(player: PlayerInfo, referee: Referee): Boolean {
        TODO("Not yet implemented")
    }
}

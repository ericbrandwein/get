package gamelogic

enum class Color{White, Black, Red, Blue, Green, Yellow, Brown, Gray}

class PlayerInfo(val name: String, val color:Color, val goal: Goal) {
    fun reachedTheGoal(referee:Referee) {
        goal.achieved(this, referee)
    }
}
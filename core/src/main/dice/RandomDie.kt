package dice

import kotlin.random.Random

class RandomDie : Die {
    override fun roll(): Int = Random.nextInt(1, 6)
}

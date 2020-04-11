package dice

import kotlin.math.min

class FixedDie(private vararg val results: Int) : Die {
    private var timesCalled = 0

    override fun roll(): Int {
        timesCalled++
        return results[min(timesCalled, results.size) - 1]
    }
}

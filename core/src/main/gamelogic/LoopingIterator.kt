package gamelogic

fun <T> MutableList<T>.loopingIterator() = LoopingIterator(this)

/**
 * An iterator on a [MutableList] that returns to the first element
 * when calling [LoopingIterator.next] on the last element.
 */
class LoopingIterator<T>(private val list: MutableList<T>) : ListIterator<T> {

    init {
        assertListIsNotEmpty()
    }

    var currentIndex = 0
        private set
    val current: T
        get() = list[currentIndex]

    override fun hasNext() = true
    override fun nextIndex() = (currentIndex + 1) % list.size
    override fun next(): T {
        currentIndex = nextIndex()
        return current
    }

    override fun hasPrevious() = true
    override fun previousIndex() =
        if (currentIndex == 0) list.lastIndex else currentIndex - 1
    override fun previous(): T {
        currentIndex = previousIndex()
        return current
    }

    fun remove() = removeAt(currentIndex)

    fun removeAt(index: Int) {
        assertListHasMoreThanOneElement()
        list.removeAt(index)
        if (index < currentIndex) {
            currentIndex--
        }
        if (currentIndex == list.size) {
            currentIndex = 0
        }
    }

    private fun assertListIsNotEmpty() {
        if (list.isEmpty()) {
            throw UnsupportedOperationException(
                "Can't create a LoopingIterator of an empty List.")
        }
    }

    private fun assertListHasMoreThanOneElement() {
        if (list.size <= 1) {
            throw UnsupportedOperationException(
                "Can't remove last element without invalidating the LoopingIterator.")
        }
    }
}

package gamelogic

fun <T> Collection<T>.loopingIterator() = LoopingIterator(this)

class LoopingIterator<T>(
    private val collection: Collection<T>
) : Iterator<T> {

    init {
        assertIsNotEmpty(collection)
    }

    private var iterator = collection.iterator()
    private var current: T = iterator.next()

    fun get() = current

    override fun hasNext() = true
    override fun next(): T {
        if (!iterator.hasNext()) {
            iterator = collection.iterator()
        }
        current = iterator.next()
        return current
    }
}

private fun <T> assertIsNotEmpty(collection: Collection<T>) {
    if (collection.isEmpty()) {
        throw UnsupportedOperationException(
            "Can't create a LoopingIterator of an empty Iterable.")
    }
}

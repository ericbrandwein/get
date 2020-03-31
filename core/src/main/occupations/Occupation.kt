package occupations


/**
 * Represents an occupation of some players in a country.
 *
 * The country can be occupied or not and can be shared or not.
 */
interface Occupation {
    fun isOccupied() = true
    fun isShared() = false
    fun accept(visitor: OccupationVisitor)
}

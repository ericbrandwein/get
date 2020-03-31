package occupations

/**
 * An Occupation where there is no player occupying the country.
 *
 * You can't manage armies in this Occupation, because there aren't any.
 */
class NoOccupation : Occupation {
    override fun isOccupied() = false

    override fun accept(visitor: OccupationVisitor) = visitor.visit(this)
}

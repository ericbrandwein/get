package occupations

interface OccupationVisitor {
    fun visit(occupation: NoOccupation)
    fun visit(occupation: SinglePlayerOccupation)
    fun visit(occupation: SharedOccupation)
}

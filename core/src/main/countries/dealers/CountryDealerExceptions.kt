package countries.dealers

class NoPlayersToDealToException : Exception(
    "Can't deal to an empty collection of players.")

package screens.running

import Country

interface CountrySelectionListener {
    fun onCountrySelected(country: Country)
}

class NoCountrySelectionListener: CountrySelectionListener {
    override fun onCountrySelected(country: Country) {}
}

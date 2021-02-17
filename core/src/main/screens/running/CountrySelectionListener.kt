package screens.running

import Country

interface CountrySelectionListener {
    fun onCountrySelected(country: Country)
    fun onCountryMouseOver(country: Country)
    fun onCountryExit(country: Country)
}

package com.example.sunandmoon.data

import com.example.sunandmoon.data.localDatabase.dao.ProductionOrderBy
import com.example.sunandmoon.data.localDatabase.dao.ShootOrderBy
import com.example.sunandmoon.data.dataUtil.Production
import com.example.sunandmoon.data.dataUtil.Shoot
import com.example.sunandmoon.model.locationForecastModel.LocationForecast
import com.example.sunandmoon.viewModel.SelectionPages

// currentPageIndex is 0 if on "productions" page or 1 if on "all shoots" page
data class ShootSelectionUIState (
    val currentPageIndex: Int = SelectionPages.PRODUCTIONS.ordinal,
    val productionsList: List<Production> = listOf(),
    val productionShootsList: List<Shoot> = listOf(),
    val soloShootsList: List<Shoot> = listOf(),
    val selectedProduction: Production? = null,
    val newProductionName: String? = null,

    val orderByDropdownOpened: Boolean = false,
    val productionOrderBy: ProductionOrderBy = ProductionOrderBy.START_DATE_TIME,
    val shootOrderBy: ShootOrderBy = ShootOrderBy.DATE_TIME,

    val shootToShowPreferredWeatherDialogFor: Shoot? = null,
    val weatherData: LocationForecast? = null,

    val searchQuery: String = ""
)

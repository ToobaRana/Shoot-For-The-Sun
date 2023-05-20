package com.example.sunandmoon.data

import com.example.sunandmoon.data.localDatabase.dao.ProductionOrderBy
import com.example.sunandmoon.data.localDatabase.dao.ShootOrderBy
import com.example.sunandmoon.data.util.Production
import com.example.sunandmoon.data.util.Shoot
import com.example.sunandmoon.model.LocationForecastModel.LocationForecast
import com.example.sunandmoon.viewModel.SelectionPages

// currentPageIndex is 0 if on "productions" page or 1 if on "all shoots" page
data class ProductionSelectionUIState (
    val currentPageIndex: Int = SelectionPages.PRODUCTIONS.ordinal,
    val productionsList: List<Production> = listOf(),
    val productionShootsList: List<Shoot> = listOf(),
    val independentShootsList: List<Shoot> = listOf(),
    val selectedProduction: Production? = null,
    val newProductionName: String? = null,

    val productionOrderBy: ProductionOrderBy = ProductionOrderBy.START_DATE_TIME,
    val shootOrderBy: ShootOrderBy = ShootOrderBy.DATE_TIME,

    val showPreferredWeatherDialog: Boolean = false,
    val weatherData: LocationForecast? = null
)

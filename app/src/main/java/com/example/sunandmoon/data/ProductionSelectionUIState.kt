package com.example.sunandmoon.data

import com.example.sunandmoon.data.localDatabase.dao.ProductionOrderBy
import com.example.sunandmoon.data.localDatabase.dao.ShootOrderBy
import com.example.sunandmoon.data.util.Production
import com.example.sunandmoon.data.util.Shoot
import com.example.sunandmoon.viewModel.SelectionPages

data class ProductionSelectionUIState (
    val currentPageIndex: Int = SelectionPages.PRODUCTIONS.ordinal,
    val productionsList: List<Production> = listOf(),
    val productionShootsList: List<Shoot> = listOf(),
    val independentShootsList: List<Shoot> = listOf(),
    val selectedProduction: Production? = null,
    val productionOrderBy: ProductionOrderBy = ProductionOrderBy.START_DATE_TIME,
    val shootOrderBy: ShootOrderBy = ShootOrderBy.DATE_TIME,
)
package com.example.sunandmoon.data

import com.example.sunandmoon.data.util.Production
import com.example.sunandmoon.data.util.Shoot

// currentPageIndex is 0 if on "productions" page or 1 if on "all shoots" page
data class ProductionSelectionUIState (
    val currentPageIndex: Int = 0,
    val productionsList: List<Production>,
    val shootsList: List<Shoot>
)

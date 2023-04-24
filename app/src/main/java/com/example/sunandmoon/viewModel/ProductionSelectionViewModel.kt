package com.example.sunandmoon.viewModel

import androidx.lifecycle.ViewModel
import com.example.sunandmoon.data.DataSource
import com.example.sunandmoon.data.ProductionSelectionUIState
import com.example.sunandmoon.data.util.Shoot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProductionSelectionViewModel : ViewModel() {
    private val dataSource = DataSource()

    //sunDataSource.fetchSunrise3Data("sun", 59.933333, 10.716667, "2022-12-18", "+01:00" ).properties.sunrise.time
    private val _productionSelectionUIState = MutableStateFlow(
        ProductionSelectionUIState(
            productionsList = listOf(Shoot(), Shoot(name = "BTS music video"))
        )
    )

    val productionSelectionUIState: StateFlow<ProductionSelectionUIState> = _productionSelectionUIState.asStateFlow()

    init {
        // get previously saved productions
    }
}
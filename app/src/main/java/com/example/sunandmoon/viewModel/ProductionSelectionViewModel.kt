package com.example.sunandmoon.viewModel

import androidx.lifecycle.ViewModel
import com.example.sunandmoon.data.DataSource
import com.example.sunandmoon.data.ProductionSelectionUIState
import com.example.sunandmoon.data.util.Production
import com.example.sunandmoon.data.util.Shoot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProductionSelectionViewModel : ViewModel() {
    private val dataSource = DataSource()

    val testShoot1 = Shoot()
    val testShoot2 = Shoot(name = "testTestTestTestTestTestTestTestTestTestTestTest")
    val testProduction1 = Production(shoots = listOf(testShoot1, testShoot2))
    val testProduction2 = Production()

    //sunDataSource.fetchSunrise3Data("sun", 59.933333, 10.716667, "2022-12-18", "+01:00" ).properties.sunrise.time
    private val _productionSelectionUIState = MutableStateFlow(
        ProductionSelectionUIState(
            shootsList = listOf(testShoot1, testShoot2),
            productionsList = listOf() //listOf(testProduction1, testProduction2)
        )
    )

    val productionSelectionUIState: StateFlow<ProductionSelectionUIState> = _productionSelectionUIState.asStateFlow()

    init {
        // get previously saved productions
    }

    fun changeCurrentPageIndex() {
        _productionSelectionUIState.update { currentState ->
            currentState.copy(
                currentPageIndex = (_productionSelectionUIState.value.currentPageIndex + 1) % 2
            )
        }
    }
}
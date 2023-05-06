package com.example.sunandmoon.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sunandmoon.data.DataSource
import com.example.sunandmoon.data.ProductionSelectionUIState
import com.example.sunandmoon.data.util.Production
import com.example.sunandmoon.data.util.Shoot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.example.sunandmoon.data.localDatabase.AppDatabase
import com.example.sunandmoon.data.localDatabase.dao.ProductionDao
import com.example.sunandmoon.data.localDatabase.dataEntities.StorableProduction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductionSelectionViewModel @Inject constructor(
    private val database: AppDatabase
) : ViewModel() {
    private val dataSource = DataSource()

    val productionDao: ProductionDao = database.productionDao()

    val testShoot1 = Shoot()
    val testShoot2 = Shoot(name = "testTestTestTestTestTestTestTestTestTestTestTest")
    val testProduction1 = Production()
    val testProduction2 = Production()

    //sunDataSource.fetchSunrise3Data("sun", 59.933333, 10.716667, "2022-12-18", "+01:00" ).properties.sunrise.time
    private val _productionSelectionUIState = MutableStateFlow(
        ProductionSelectionUIState(
            productionsList = listOf(), //listOf(testProduction1, testProduction2),
            shootsList = listOf(testShoot1, testShoot2)
        )
    )

    val productionSelectionUIState: StateFlow<ProductionSelectionUIState> = _productionSelectionUIState.asStateFlow()

    init {
        // get previously saved productions
        //saveProduction()
        getAllProductions()
    }

    fun getAllProductions() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val allProductions = productionDao.getAll()
                var productionList = mutableListOf<Production>()
                allProductions.forEach() { storableProduction ->
                    productionList.add(
                        Production(
                            id = storableProduction.uid,
                            name = storableProduction.name,
                            duration = Pair(
                                storableProduction.startDateTime,
                                storableProduction.endDateTime
                            )
                        )
                    )
                }
                withContext(Dispatchers.Main) {
                    _productionSelectionUIState.update { currentState ->
                        currentState.copy(
                            productionsList = productionList
                        )
                    }
                }
            }
        }
    }

    fun saveProduction() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                productionDao.insert(StorableProduction(
                    uid = 0,
                    name = "test2",
                    startDateTime = null,
                    endDateTime = null
                ))
                getAllProductions()
            }
        }
    }

    fun changeCurrentPageIndex() {
        _productionSelectionUIState.update { currentState ->
            currentState.copy(
                currentPageIndex = (_productionSelectionUIState.value.currentPageIndex + 1) % 2
            )
        }
    }
}
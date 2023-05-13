package com.example.sunandmoon.viewModel

import android.location.Location
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
import com.example.sunandmoon.data.localDatabase.dao.ShootDao
import com.example.sunandmoon.data.localDatabase.dataEntities.StorableProduction
import com.example.sunandmoon.data.localDatabase.dataEntities.StorableShoot
import com.example.sunandmoon.data.localDatabase.storableShootsToNormalShoots
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

enum class SelectionPages {
    PRODUCTIONS, SHOOTS, PRODUCTION_SHOOTS
}

@HiltViewModel
class ProductionSelectionViewModel @Inject constructor(
    private val database: AppDatabase
) : ViewModel() {
    private val dataSource = DataSource()

    val productionDao: ProductionDao = database.productionDao()
    val shootDao: ShootDao = database.shootDao()

    private val _productionSelectionUIState = MutableStateFlow(
        ProductionSelectionUIState()
    )

    val productionSelectionUIState: StateFlow<ProductionSelectionUIState> = _productionSelectionUIState.asStateFlow()

    init {
        // get previously saved productions
        //saveProduction()
        getAllProductions()
        getAllIndependentShoots()
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

    fun getAllIndependentShoots() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val allIndependentShoots = shootDao.getAllIndependentShoots()
                val shootList = storableShootsToNormalShoots(allIndependentShoots)
                withContext(Dispatchers.Main) {
                    _productionSelectionUIState.update { currentState ->
                        currentState.copy(
                            independentShootsList = shootList
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
                    name = "test " + (Math.random()*1000).toInt().toString(),
                    startDateTime = null,
                    endDateTime = null
                ))
                getAllProductions()
            }
        }
    }

    fun deleteProduction() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val idToDelete: Int? = _productionSelectionUIState.value.selectedProduction?.id
                Log.i("aaa12345", idToDelete.toString())
                if(idToDelete != null) {
                    shootDao.deleteShootsInProduction(idToDelete)
                    productionDao.delete(productionDao.loadById(idToDelete))
                    getAllProductions()
                }
            }
            goOutOfProduction()
        }
    }

    fun changeCurrentPageIndex() {
        _productionSelectionUIState.update { currentState ->
            val newPageIndex = (
                    if(_productionSelectionUIState.value.currentPageIndex == 0)
                        SelectionPages.SHOOTS.ordinal
                    else
                        SelectionPages.PRODUCTIONS.ordinal
                )

            currentState.copy(
                currentPageIndex = newPageIndex
            )
        }
    }

    fun getShootsInProduction(production: Production) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val productionShoots = production.id?.let { shootDao.loadByProductionId(it) }
                val shootList = storableShootsToNormalShoots(productionShoots)
                withContext(Dispatchers.Main) {
                    _productionSelectionUIState.update { currentState ->
                        currentState.copy(
                            productionShootsList = shootList
                        )
                    }
                }
            }
        }

    }

    fun goIntoProduction(production: Production) {
        _productionSelectionUIState.update { currentState ->
            currentState.copy(
                selectedProduction = production,
                currentPageIndex = SelectionPages.PRODUCTION_SHOOTS.ordinal,
            )
        }
        getShootsInProduction(production)
    }

    fun goOutOfProduction() {
        _productionSelectionUIState.update { currentState ->
            currentState.copy(
                currentPageIndex = SelectionPages.PRODUCTIONS.ordinal,
                selectedProduction = null
            )
        }
    }
}
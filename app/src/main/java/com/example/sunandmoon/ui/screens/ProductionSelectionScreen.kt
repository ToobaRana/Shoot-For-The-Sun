package com.example.sunandmoon.ui.screens

import android.location.Location
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sunandmoon.R
import com.example.sunandmoon.data.util.Shoot
import com.example.sunandmoon.ui.components.CalendarComponent
import com.example.sunandmoon.ui.components.NavigationComposable
import com.example.sunandmoon.ui.components.SunCard
import com.example.sunandmoon.ui.components.infoComponents.ProductionCard
import com.example.sunandmoon.viewModel.ProductionSelectionViewModel
import com.example.sunandmoon.viewModel.ShootInfoViewModel
import java.time.LocalDateTime



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductionSelectionScreen(modifier: Modifier, navigateToShootInfoScreen: (shoot: Shoot) -> Unit, productionSelectionViewModel: ProductionSelectionViewModel = viewModel(), navigateToNextBottomBar: (index: Int) -> Unit){

    val productionSelectionUIState by productionSelectionViewModel.productionSelectionUIState.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column(modifier.fillMaxWidth()) {
                TextField(
                    value = "Choose event",
                    onValueChange = { query ->
                    },
                    singleLine = true,
                    modifier = modifier.fillMaxWidth(0.8f).align(Alignment.CenterHorizontally),
                    leadingIcon = {
                        Icon(painterResource(R.drawable.find_shoot_icon), "location search field icon", Modifier, MaterialTheme.colorScheme.primary)
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        //cursorColor = MaterialTheme.colorScheme.primary,
                        textColor = MaterialTheme.colorScheme.onSurface,
                        containerColor = MaterialTheme.colorScheme.background,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        },
        content = {innerPadding ->
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(top = 20.dp)
            )
            {
                items(productionSelectionUIState.productionsList) { production ->
                    ProductionCard(modifier, production, navigateToShootInfoScreen)
                }
            }
        },
        bottomBar = {
            NavigationComposable(page = 0, navigateToNextBottomBar)
        }
    )
}
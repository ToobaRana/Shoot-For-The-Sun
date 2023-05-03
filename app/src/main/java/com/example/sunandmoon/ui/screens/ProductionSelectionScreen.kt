package com.example.sunandmoon.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sunandmoon.R
import com.example.sunandmoon.data.ProductionSelectionUIState
import com.example.sunandmoon.data.util.Shoot
import com.example.sunandmoon.ui.components.NavigationComposable
import com.example.sunandmoon.ui.components.infoComponents.ShootCard
import com.example.sunandmoon.viewModel.ProductionSelectionViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductionSelectionScreen(
    modifier: Modifier,
    navigateToShootInfoScreen: (shoot: Shoot) -> Unit,
    productionSelectionViewModel: ProductionSelectionViewModel = viewModel(),
    navigateToNextBottomBar: (index: Int) -> Unit,
    navigateToCreateShootScreen: () -> Unit,
    navigateToCreateProductionScreen: () -> Unit
){

    val productionSelectionUIState by productionSelectionViewModel.productionSelectionUIState.collectAsState()

    val pageTitleTexts = listOf("Your produtions", "Individual shoots")

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column(modifier.fillMaxWidth()) {
                Text(
                    text = pageTitleTexts[productionSelectionUIState.currentPageIndex],
                    fontSize = 35.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(30.dp),
                    fontWeight = FontWeight(500)
                )
                TextField(
                    value = "",
                    placeholder = {Text("Search...", color = MaterialTheme.colorScheme.primary)},
                    onValueChange = { query ->
                    },
                    singleLine = true,
                    modifier = modifier
                        .fillMaxWidth(0.8f)
                        .align(Alignment.CenterHorizontally),
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

                Box(
                    modifier
                        .fillMaxWidth()
                        .padding(20.dp), contentAlignment = Alignment.Center) {
                    Row(
                        modifier
                            .width(320.dp)
                            .height(75.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {

                        Button(onClick = {
                            navigateToCreateShootScreen()
                        }, modifier.weight(1f), shape = RoundedCornerShape(15.dp)) {
                            Row() {
                                Icon(painterResource(R.drawable.plus_icon), "location search field icon", modifier.size(24.dp), MaterialTheme.colorScheme.onPrimary)
                                Text("  Add new", fontSize = 16.sp, )
                            }
                        }
                        Spacer(modifier = modifier.weight(0.2f))
                        FilledIconButton(onClick = { /* TODO */ }, modifier.weight(1f), shape = RoundedCornerShape(15.dp)) {
                            Text("Order by", fontSize = 16.sp)
                        }
                    }
                }
            }
        },
        content = {innerPadding ->
            if(productionSelectionUIState.currentPageIndex == 0 && productionSelectionUIState.productionsList.isEmpty()) {
                Box(modifier = modifier.fillMaxSize()){
                    Text("Empty...\n\n(Add a production)", color = MaterialTheme.colorScheme.primary, modifier = modifier.align(Alignment.Center), textAlign = TextAlign.Center)
                }
            }
            else if(productionSelectionUIState.currentPageIndex == 1 && productionSelectionUIState.shootsList.isEmpty()) {
                Box(modifier = modifier.fillMaxSize()){
                    Text("Empty...\n\n(Add a shoot)", color = MaterialTheme.colorScheme.primary, modifier = modifier.align(Alignment.Center), textAlign = TextAlign.Center)
                }
            }
            else {
                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
                {
                    if (productionSelectionUIState.currentPageIndex == 0) {
                        items(productionSelectionUIState.productionsList) { production ->
                            //ShootCard(modifier, production, navigateToShootInfoScreen)
                        }
                    } else {
                        items(productionSelectionUIState.shootsList) { production ->
                            ShootCard(modifier, production, navigateToShootInfoScreen)
                        }
                    }
                }
            }
        },
        bottomBar = {
            Column(modifier.fillMaxWidth()) {
                PagePickerProductionShoots(modifier, productionSelectionViewModel, productionSelectionUIState)
                NavigationComposable(page = 0, navigateToNextBottomBar)
            }
        }
    )
}

@Composable
fun PagePickerProductionShoots(modifier: Modifier, productionSelectionViewModel: ProductionSelectionViewModel = viewModel(), productionSelectionUIState: ProductionSelectionUIState){
    Box(
        modifier
            .fillMaxWidth()
            .padding(20.dp), contentAlignment = Alignment.Center) {
        Row(
            modifier
                .width(320.dp)
                .height(35.dp)
                .background(MaterialTheme.colorScheme.tertiary, RoundedCornerShape(15.dp)),
            horizontalArrangement = Arrangement.Start
        ) {
            val currentPageIndex = productionSelectionUIState.currentPageIndex
            val colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.tertiary)
            Button(onClick = { if(currentPageIndex != 0) productionSelectionViewModel.changeCurrentPageIndex() }, modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = colors[currentPageIndex])) {
                Text("Productions", color = colors[(currentPageIndex + 1) % 2], fontSize = 14.sp)
            }
            Button(onClick = { if(currentPageIndex != 1) productionSelectionViewModel.changeCurrentPageIndex() }, modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = colors[(currentPageIndex + 1) % 2])) {
                Text("Individual Shoots", color = colors[currentPageIndex], fontSize = 14.sp)
            }
        }
    }
}


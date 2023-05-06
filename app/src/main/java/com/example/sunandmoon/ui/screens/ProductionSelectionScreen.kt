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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sunandmoon.R
import com.example.sunandmoon.data.ProductionSelectionUIState
import com.example.sunandmoon.data.util.Shoot
import com.example.sunandmoon.ui.components.NavigationComposable
import com.example.sunandmoon.ui.components.infoComponents.ProductionCard
import com.example.sunandmoon.ui.components.infoComponents.ShootCard
import com.example.sunandmoon.viewModel.ProductionSelectionViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductionSelectionScreen(
    modifier: Modifier,
    navigateToShootInfoScreen: (shoot: Shoot) -> Unit,
    productionSelectionViewModel: ProductionSelectionViewModel = hiltViewModel(),
    navigateToNextBottomBar: (index: Int) -> Unit,
    navigateToCreateShootScreen: () -> Unit,
){

    val productionSelectionUIState by productionSelectionViewModel.productionSelectionUIState.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
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
                    item { ProductionShootSelectionTopPart(modifier, navigateToCreateShootScreen, productionSelectionViewModel, productionSelectionUIState) }
                    if (productionSelectionUIState.currentPageIndex == 0) {
                        items(productionSelectionUIState.productionsList) { production ->
                            ProductionCard(modifier, production)
                        }
                    } else {
                        items(productionSelectionUIState.shootsList) { shoot ->
                            ShootCard(modifier, shoot, navigateToShootInfoScreen)
                        }
                    }
                }
            }
        },
        bottomBar = {
            Column(modifier.fillMaxWidth()) {
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
            .padding(20.dp,30.dp,20.dp,10.dp),
        contentAlignment = Alignment.Center
    )
    {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductionShootSelectionTopPart(
    modifier: Modifier,
    navigateToCreateShootScreen: () -> Unit,
    productionSelectionViewModel: ProductionSelectionViewModel = hiltViewModel(),
    productionSelectionUIState: ProductionSelectionUIState
) {
    val pageTitleTexts = listOf("Your produtions", "Individual shoots")

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
        PagePickerProductionShoots(modifier, productionSelectionViewModel, productionSelectionUIState)
        Box(
            modifier
                .fillMaxWidth()
                .padding(20.dp,20.dp,20.dp,10.dp), contentAlignment = Alignment.Center) {
            Row(
                modifier
                    .width(320.dp)
                    .height(45.dp),
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
}
package com.example.sunandmoon.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sunandmoon.R
import com.example.sunandmoon.data.ProductionSelectionUIState
import com.example.sunandmoon.data.util.Shoot
import com.example.sunandmoon.ui.components.NavigationComposable
import com.example.sunandmoon.ui.components.infoComponents.ProductionCard
import com.example.sunandmoon.ui.components.infoComponents.ShootCard
import com.example.sunandmoon.viewModel.ProductionSelectionViewModel
import com.example.sunandmoon.viewModel.SelectionPages


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductionSelectionScreen(
    modifier: Modifier,
    navigateToShootInfoScreen: (shoot: Shoot) -> Unit,
    productionSelectionViewModel: ProductionSelectionViewModel = hiltViewModel(),
    navigateToNextBottomBar: (index: Int) -> Unit,
    navigateToCreateShootScreen: () -> Unit,
) {

    val productionSelectionUIState by productionSelectionViewModel.productionSelectionUIState.collectAsState()

    val currentPageIndex = productionSelectionUIState.currentPageIndex
    val currentPageIsEmpty: Boolean =
        (currentPageIndex == SelectionPages.PRODUCTIONS.ordinal && productionSelectionUIState.productionsList.isEmpty())
                || (currentPageIndex == SelectionPages.SHOOTS.ordinal && productionSelectionUIState.independentShootsList.isEmpty())
                || (currentPageIndex == SelectionPages.PRODUCTION_SHOOTS.ordinal && productionSelectionUIState.productionShootsList.isEmpty())

    if(currentPageIndex == SelectionPages.PRODUCTION_SHOOTS.ordinal) {
        BackHandler(enabled = true, onBack = { productionSelectionViewModel.goOutOfProduction() })
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            if (currentPageIsEmpty) {
                ProductionShootSelectionTopPart(
                    modifier,
                    navigateToCreateShootScreen,
                    productionSelectionViewModel,
                    currentPageIndex,
                    productionSelectionUIState
                )
            }
        },
        content = { innerPadding ->
            if (currentPageIndex == SelectionPages.PRODUCTIONS.ordinal && productionSelectionUIState.productionsList.isEmpty()) {
                Box(modifier = modifier.fillMaxSize()) {
                    Text(
                        "Empty...\n\n(Add a production)",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = modifier.align(Alignment.Center),
                        textAlign = TextAlign.Center
                    )
                }
            } else if (currentPageIndex == SelectionPages.SHOOTS.ordinal && productionSelectionUIState.independentShootsList.isEmpty()) {
                Box(modifier = modifier.fillMaxSize()) {
                    Text(
                        "Empty...\n\n(Add a shoot)",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = modifier.align(Alignment.Center),
                        textAlign = TextAlign.Center
                    )
                }
            } else if (currentPageIndex == SelectionPages.PRODUCTION_SHOOTS.ordinal && productionSelectionUIState.productionShootsList.isEmpty()) {
                Box(modifier = modifier.fillMaxSize()) {
                    Text(
                        "Empty...\n\n(Add a shoot to this production)",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = modifier.align(Alignment.Center),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
                {
                    item {
                        ProductionShootSelectionTopPart(
                            modifier,
                            navigateToCreateShootScreen,
                            productionSelectionViewModel,
                            currentPageIndex,
                            productionSelectionUIState
                        )
                    }
                    if (currentPageIndex == SelectionPages.PRODUCTIONS.ordinal) {
                        items(productionSelectionUIState.productionsList) { production ->
                            ProductionCard(
                                modifier,
                                production
                            ) { productionSelectionViewModel.goIntoProduction() }
                        }
                    } else if (currentPageIndex == SelectionPages.SHOOTS.ordinal) {
                        items(productionSelectionUIState.independentShootsList) { shoot ->
                            ShootCard(modifier, shoot, navigateToShootInfoScreen)
                        }
                    } else {
                        items(productionSelectionUIState.productionShootsList) { shoot ->
                            ShootCard(modifier, shoot, navigateToShootInfoScreen)
                        }
                    }
                }
            }
        },
        bottomBar = {
            if (currentPageIndex != SelectionPages.PRODUCTION_SHOOTS.ordinal) {
                Column(modifier.fillMaxWidth()) {
                    NavigationComposable(page = 0, navigateToNextBottomBar)
                }
            }
        }
    )
}

@Composable
fun PagePickerProductionShoots(
    modifier: Modifier,
    productionSelectionViewModel: ProductionSelectionViewModel = viewModel(),
    currentPageIndex: Int
) {
    Box(
        modifier
            .fillMaxWidth()
            .padding(20.dp, 30.dp, 20.dp, 10.dp),
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
            val colors =
                listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.tertiary)
            Button(
                onClick = { if (currentPageIndex != SelectionPages.PRODUCTIONS.ordinal) productionSelectionViewModel.changeCurrentPageIndex() },
                modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = colors[currentPageIndex])
            ) {
                Text("Productions", color = colors[(currentPageIndex + 1) % 2], fontSize = 14.sp)
            }
            Button(
                onClick = { if (currentPageIndex != SelectionPages.SHOOTS.ordinal) productionSelectionViewModel.changeCurrentPageIndex() },
                modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = colors[(currentPageIndex + 1) % 2])
            ) {
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
    currentPageIndex: Int,
    productionSelectionUIState: ProductionSelectionUIState
) {
    val pageTitleTexts = listOf("Your productions", "Independent shoots", "My production")
    var titleTextToUse: String = pageTitleTexts[currentPageIndex]
    if (currentPageIndex == SelectionPages.PRODUCTION_SHOOTS.ordinal) {
        titleTextToUse = productionSelectionUIState.selectedProduction?.name ?: "My production"
    }

    Column(modifier.fillMaxWidth()) {
        Text(
            text = titleTextToUse,
            fontSize = 35.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .padding(30.dp),
            fontWeight = FontWeight(500)
        )
        TextField(
            value = "",
            placeholder = { Text("Search...", color = MaterialTheme.colorScheme.primary) },
            onValueChange = { query ->
            },
            singleLine = true,
            modifier = modifier
                .fillMaxWidth(0.8f)
                .align(Alignment.CenterHorizontally),
            leadingIcon = {
                Icon(
                    painterResource(R.drawable.find_shoot_icon),
                    "location search field icon",
                    Modifier,
                    MaterialTheme.colorScheme.primary
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                //cursorColor = MaterialTheme.colorScheme.primary,
                textColor = MaterialTheme.colorScheme.onSurface,
                containerColor = MaterialTheme.colorScheme.background,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurface
            )
        )
        if (currentPageIndex != SelectionPages.PRODUCTION_SHOOTS.ordinal) {
            PagePickerProductionShoots(modifier, productionSelectionViewModel, currentPageIndex)
        }
        Box(
            modifier
                .fillMaxWidth()
                .padding(20.dp, 20.dp, 20.dp, 10.dp), contentAlignment = Alignment.Center
        ) {
            Row(
                modifier
                    .width(320.dp)
                    .height(45.dp),
                horizontalArrangement = Arrangement.Start
            ) {

                Button(onClick = {
                    if (currentPageIndex == SelectionPages.PRODUCTIONS.ordinal) {
                        productionSelectionViewModel.saveProduction()
                    } else {
                        navigateToCreateShootScreen()
                    }
                }, modifier.weight(1f), shape = RoundedCornerShape(15.dp)) {
                    Row() {
                        Icon(
                            painterResource(R.drawable.plus_icon),
                            "location search field icon",
                            modifier.size(24.dp),
                            MaterialTheme.colorScheme.onPrimary
                        )
                        Text("  Add new", fontSize = 16.sp)
                    }
                }
                Spacer(modifier = modifier.weight(0.2f))
                FilledIconButton(
                    onClick = { /* TODO */ },
                    modifier.weight(1f),
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Text("Order by", fontSize = 16.sp)
                }
            }
        }
    }
}
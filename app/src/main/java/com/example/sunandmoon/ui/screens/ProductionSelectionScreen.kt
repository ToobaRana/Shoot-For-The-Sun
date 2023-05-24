package com.example.sunandmoon.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sunandmoon.R
import com.example.sunandmoon.ui.components.NavigationComposable
import com.example.sunandmoon.ui.components.ProductionShootSelectionTopPart

import com.example.sunandmoon.ui.components.infoComponents.PreferredWeatherDialog
import com.example.sunandmoon.ui.components.infoComponents.ProductionCard
import com.example.sunandmoon.ui.components.infoComponents.ShootCard
import com.example.sunandmoon.viewModel.ShootSelectionViewModel
import com.example.sunandmoon.viewModel.SelectionPages


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShootSelectionScreen(
    modifier: Modifier,
    navigateToShootInfoScreen: (shootId: Int) -> Unit,
    shootSelectionViewModel: ShootSelectionViewModel = hiltViewModel(),
    navigateToNextBottomBar: (index: Int) -> Unit,
    navigateToCreateShootScreen: (parentProductionId: Int?, shootToEditId: Int?) -> Unit,
    navController: NavController,
    currentScreenRoute: String,
    goToAboutScreen: () -> Unit
) {

    val shootSelectionUIState by shootSelectionViewModel.shootSelectionUIState.collectAsState()

    // when you navigate back to this screen we want to get the shoots from the database again
    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.route == currentScreenRoute) {
                // get all the shoots from the database when popped back to this screen
                shootSelectionViewModel.getAllSoloShoots()
                // if you were inside a production. Get all the shoot within this production too
                if (shootSelectionUIState.selectedProduction != null) {
                    shootSelectionViewModel.getShootsInProduction(shootSelectionUIState.selectedProduction!!)
                    // We also need to get the productions again so that their date interval is correct
                    shootSelectionViewModel.getAllProductions()
                }
            }
        }
    }

    val currentPageIndex = shootSelectionUIState.currentPageIndex
    val currentPageIsEmpty: Boolean =
        (currentPageIndex == SelectionPages.PRODUCTIONS.ordinal && shootSelectionUIState.productionsList.isEmpty())
                || (currentPageIndex == SelectionPages.SHOOTS.ordinal && shootSelectionUIState.soloShootsList.isEmpty())
                || (currentPageIndex == SelectionPages.PRODUCTION_SHOOTS.ordinal && shootSelectionUIState.productionShootsList.isEmpty())

    if (currentPageIndex == SelectionPages.PRODUCTION_SHOOTS.ordinal) {
        BackHandler(enabled = true, onBack = { shootSelectionViewModel.goOutOfProduction() })
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            var emptyAddSomethingText: String? = null
            if (currentPageIndex == SelectionPages.PRODUCTIONS.ordinal && shootSelectionUIState.productionsList.isEmpty()) {
                emptyAddSomethingText = stringResource(id = R.string.EmptyAddProduction)
            } else if (currentPageIndex == SelectionPages.SHOOTS.ordinal && shootSelectionUIState.soloShootsList.isEmpty()) {
                emptyAddSomethingText = stringResource(id = R.string.EmptyAddShoot)
            } else if (currentPageIndex == SelectionPages.PRODUCTION_SHOOTS.ordinal && shootSelectionUIState.productionShootsList.isEmpty()) {
                emptyAddSomethingText = stringResource(id = R.string.EmptyAddShootToProduction)
            }

            if (currentPageIsEmpty) {
                Column(modifier.fillMaxSize()) {
                    ProductionShootSelectionTopPart(
                        modifier,
                        navigateToCreateShootScreen,
                        shootSelectionViewModel,
                        currentPageIndex,
                        shootSelectionUIState,
                        goToAboutScreen
                    )
                    if (emptyAddSomethingText != null) {
                        Column(modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                            Spacer(modifier.size(80.dp))
                            Text(
                                emptyAddSomethingText,
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        },
        content = { innerPadding ->
            if(!currentPageIsEmpty) {
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
                            shootSelectionViewModel,
                            currentPageIndex,
                            shootSelectionUIState,
                            goToAboutScreen
                        )
                    }
                    when (currentPageIndex) {
                        SelectionPages.PRODUCTIONS.ordinal -> {
                            items(shootSelectionUIState.productionsList) { production ->
                                ProductionCard(
                                    modifier,
                                    production
                                ) { shootSelectionViewModel.goIntoProduction(production) }
                            }
                        }
                        SelectionPages.SHOOTS.ordinal -> {
                            items(shootSelectionUIState.soloShootsList) { shoot ->
                                ShootCard(
                                    modifier,
                                    shoot,
                                    navigateToShootInfoScreen,
                                    { shootSelectionViewModel.setShowPreferredWeatherDialog(shoot) }
                                )
                            }
                        }
                        else -> {
                            items(shootSelectionUIState.productionShootsList) { shoot ->
                                ShootCard(
                                    modifier,
                                    shoot,
                                    navigateToShootInfoScreen,
                                    { shootSelectionViewModel.setShowPreferredWeatherDialog(shoot) }
                                )
                            }
                        }
                    }
                }
            }
        },
        bottomBar = {
            if (currentPageIndex != SelectionPages.PRODUCTION_SHOOTS.ordinal) {
                Column(modifier.fillMaxWidth()) {
                    NavigationComposable(modifier = modifier, page = 0, navigateToNextBottomBar)
                }
            }
        }
    )

    if(shootSelectionUIState.shootToShowPreferredWeatherDialogFor != null) {
        PreferredWeatherDialog(modifier = modifier, shootSelectionViewModel = shootSelectionViewModel, shoot = shootSelectionUIState.shootToShowPreferredWeatherDialogFor!!)
    }

    if (shootSelectionUIState.newProductionName != null) {
        productionCreation(
            modifier,
            createProduction = { name: String ->
                shootSelectionViewModel.saveProduction(name)
            },
            setProductionName = { name: String? ->
                shootSelectionViewModel.setProductionName(name)
            },
            shootSelectionUIState.newProductionName!!
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun productionCreation(
    modifier: Modifier,
    createProduction: (name: String) -> Unit,
    setProductionName: (name: String?) -> Unit,
    productionName: String
) {
    val focusManager = LocalFocusManager.current
    Box(
        modifier
            .fillMaxSize()
            .background(Color(0x88000000))
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()

                })

            },

        contentAlignment = Alignment.Center
    ) {

        Card(
            modifier = modifier
                .wrapContentSize()
                .width(320.dp)

                //.size(height = 200.dp, width = 320.dp)
                .clickable(enabled = false) { println("ja") },
            colors = CardDefaults.cardColors(

                containerColor = MaterialTheme.colorScheme.background,

                )

        ) {
            Column(
                modifier = modifier
                    .wrapContentSize()
                    .padding(15.dp),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text("Create new production", color = MaterialTheme.colorScheme.onSurface)
                TextField(
                    placeholder = { Text("My Production") },
                    modifier = modifier.clickable {},
                    value = productionName,
                    onValueChange = { newName: String ->
                        if (newName.length <= 70) {
                            setProductionName(newName)
                        }

                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    leadingIcon = {
                        Icon(
                            painterResource(R.drawable.edit_icon),
                            "Edit text pencil icon",
                            Modifier,
                            MaterialTheme.colorScheme.primary
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        //cursorColor = MaterialTheme.colorScheme.primary,
                        textColor = MaterialTheme.colorScheme.onSurface,
                        containerColor = MaterialTheme.colorScheme.background,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                        placeholderColor = MaterialTheme.colorScheme.secondary
                    ),
                    textStyle = TextStyle(fontSize = 18.sp, fontFamily = FontFamily(Font(R.font.nunito)))
                )

                val defaultProductionName = stringResource(R.string.defaultProductionName)
                Button(
                    onClick = {
                        if (productionName.isBlank()) {
                            createProduction(defaultProductionName)
                        } else {
                            createProduction(productionName)
                        }
                        //save stuff

                        setProductionName(null)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onSurface
                    )
                )

                {
                    Text(text = stringResource(id = R.string.SaveButton), fontFamily = FontFamily(Font(R.font.nunito)))
                }


            }

        }

    }


}
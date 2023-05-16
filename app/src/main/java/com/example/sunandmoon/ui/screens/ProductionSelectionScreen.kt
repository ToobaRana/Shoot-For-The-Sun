package com.example.sunandmoon.ui.screens

import android.graphics.drawable.Icon

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.ImeOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.sunandmoon.R
import com.example.sunandmoon.data.ProductionSelectionUIState
import com.example.sunandmoon.data.util.Shoot
import com.example.sunandmoon.ui.components.NavigationComposable
import com.example.sunandmoon.ui.components.ProductionShootSelectionTopPart
import com.example.sunandmoon.ui.components.buttonComponents.AddNewOrderByButtons
import com.example.sunandmoon.ui.components.buttonComponents.GoBackEditDeleteBar
import com.example.sunandmoon.ui.components.buttonComponents.PagePickerProductionsShoots
import com.example.sunandmoon.ui.components.infoComponents.ProductionCard
import com.example.sunandmoon.ui.components.infoComponents.ShootCard
import com.example.sunandmoon.viewModel.ProductionSelectionViewModel
import com.example.sunandmoon.viewModel.SelectionPages
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductionSelectionScreen(
    modifier: Modifier,
    navigateToShootInfoScreen: (shootId: Int) -> Unit,
    productionSelectionViewModel: ProductionSelectionViewModel = hiltViewModel(),
    navigateToNextBottomBar: (index: Int) -> Unit,
    navigateToCreateShootScreen: (parentProductionId: Int?, shootToEditId: Int?) -> Unit,
    navController: NavController,
    currentScreenRoute: String
) {

    val productionSelectionUIState by productionSelectionViewModel.productionSelectionUIState.collectAsState()

    // when you navigate back to this screen we want to get the shoots from the database again
    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.route == currentScreenRoute) {
                // get all the shoots from the database when popped back to this screen
                productionSelectionViewModel.getAllIndependentShoots()
                // if you were inside a production. Get all the shoot within this production too
                if(productionSelectionUIState.selectedProduction != null) {
                    productionSelectionViewModel.getShootsInProduction(productionSelectionUIState.selectedProduction!!)
                    // We also need to get the productions again so that their date interval is correct
                    productionSelectionViewModel.getAllProductions()
                }
            }
        }
    }

    val currentPageIndex = productionSelectionUIState.currentPageIndex
    val currentPageIsEmpty: Boolean =
        (currentPageIndex == SelectionPages.PRODUCTIONS.ordinal && productionSelectionUIState.productionsList.isEmpty())
                || (currentPageIndex == SelectionPages.SHOOTS.ordinal && productionSelectionUIState.independentShootsList.isEmpty())
                || (currentPageIndex == SelectionPages.PRODUCTION_SHOOTS.ordinal && productionSelectionUIState.productionShootsList.isEmpty())

    if (currentPageIndex == SelectionPages.PRODUCTION_SHOOTS.ordinal) {
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
            var emptyAddSomethingText: String? = null
            if (currentPageIndex == SelectionPages.PRODUCTIONS.ordinal && productionSelectionUIState.productionsList.isEmpty()) {
                emptyAddSomethingText = "Empty...\n\n(Add a production)"
            }
            else if (currentPageIndex == SelectionPages.SHOOTS.ordinal && productionSelectionUIState.independentShootsList.isEmpty()) {
                emptyAddSomethingText = "Empty...\n\n(Add a shoot)"
            }
            else if (currentPageIndex == SelectionPages.PRODUCTION_SHOOTS.ordinal && productionSelectionUIState.productionShootsList.isEmpty()) {
                emptyAddSomethingText = "Empty...\n\n(Add a shoot to this production)"
            }

            if(emptyAddSomethingText != null) {
                Box(modifier = modifier.fillMaxSize()) {
                    Text(
                        "Empty...\n\n(Add a production)",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = modifier.align(Alignment.Center),
                        textAlign = TextAlign.Center
                    )
                }
            }
             else {
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
                    when (currentPageIndex) {
                        SelectionPages.PRODUCTIONS.ordinal -> {
                            items(productionSelectionUIState.productionsList) { production ->
                                ProductionCard(
                                    modifier,
                                    production
                                ) { productionSelectionViewModel.goIntoProduction(production) }
                            }
                        }
                        SelectionPages.SHOOTS.ordinal -> {
                            items(productionSelectionUIState.independentShootsList) { shoot ->
                                ShootCard(modifier, shoot, navigateToShootInfoScreen)
                            }
                        }
                        else -> {
                            items(productionSelectionUIState.productionShootsList) { shoot ->
                                ShootCard(modifier, shoot, navigateToShootInfoScreen)
                            }
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

    if (productionSelectionUIState.newProductionName != null) {
        productionCreation(
            modifier,
            createProduction = { productionSelectionViewModel.saveProduction() },
            setProductionName = { name: String ->
                productionSelectionViewModel.setProductionName(name)
            },
            productionSelectionUIState.newProductionName!!
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductionShootSelectionTopPart(
    modifier: Modifier,
    navigateToCreateShootScreen: (parentProductionId: Int?, shootToEditId: Int?) -> Unit,
    productionSelectionViewModel: ProductionSelectionViewModel = hiltViewModel(),
    currentPageIndex: Int,
    productionSelectionUIState: ProductionSelectionUIState
) {
    val pageTitleTexts = listOf("Your productions", "Solo shoots", "My production")
    var titleTextToUse: String = pageTitleTexts[currentPageIndex]
    if (currentPageIndex == SelectionPages.PRODUCTION_SHOOTS.ordinal) {
        titleTextToUse = productionSelectionUIState.selectedProduction?.name ?: "My production"
    }

    Column(modifier.fillMaxWidth()) {

        if (currentPageIndex == SelectionPages.PRODUCTION_SHOOTS.ordinal) {
            GoBackEditDeleteBar(
                modifier,
                MaterialTheme.colorScheme.primary,
                { productionSelectionViewModel.goOutOfProduction() },
                { /* TODO */ },
                { productionSelectionViewModel.deleteProduction() })
        }
        Text(
            text = titleTextToUse,
            fontSize = 35.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 30.dp, top = 30.dp),
            fontWeight = FontWeight(500)
        )
        TextField(
            value = "",
            placeholder = {
                Text(
                    "Search...",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 18.sp
                )
            },
            onValueChange = { query ->
            },
            singleLine = true,
            modifier = modifier
                .fillMaxWidth(0.8f)
                .align(Alignment.CenterHorizontally),
            leadingIcon = {
                Icon(
                    painterResource(R.drawable.search),
                    "location search field icon",
                    modifier,
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
            PagePickerProductionsShoots(modifier, productionSelectionViewModel, currentPageIndex)
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
                        productionSelectionViewModel.setProductionName("My Production");
                    } else {
                        navigateToCreateShootScreen(
                            productionSelectionUIState.selectedProduction?.id,
                            null
                        )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun productionCreation(
    modifier: Modifier,
    createProduction: () -> Unit,
    setProductionName: (name: String) -> Unit,
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
                .fillMaxHeight(0.4f)
                .fillMaxWidth(0.8f)
                .clickable(enabled = false) { println("ja") },

            ) {
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Create new Production")
                TextField(
                    modifier = modifier.clickable {},
                    value = productionName,
                    onValueChange = { newName: String -> setProductionName(newName) },
                    label = { Text("Give name") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                )


            }

        }

    }


}
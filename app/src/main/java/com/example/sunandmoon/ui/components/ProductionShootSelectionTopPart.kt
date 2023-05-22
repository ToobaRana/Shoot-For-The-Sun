package com.example.sunandmoon.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sunandmoon.R
import com.example.sunandmoon.data.ProductionSelectionUIState
import com.example.sunandmoon.ui.components.buttonComponents.AboutButton
import com.example.sunandmoon.ui.components.buttonComponents.AddNewOrderByButtons
import com.example.sunandmoon.ui.components.buttonComponents.GoBackEditDeleteBar
import com.example.sunandmoon.ui.components.buttonComponents.PagePickerProductionsShoots
import com.example.sunandmoon.viewModel.ProductionSelectionViewModel
import com.example.sunandmoon.viewModel.SelectionPages

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductionShootSelectionTopPart(
    modifier: Modifier,
    navigateToCreateShootScreen: (parentProductionId: Int?, shootToEditId: Int?) -> Unit,
    productionSelectionViewModel: ProductionSelectionViewModel = hiltViewModel(),
    currentPageIndex: Int,
    productionSelectionUIState: ProductionSelectionUIState,
    goToAboutScreen: () -> Unit
) {
    val pageTitleTexts = listOf("Your productions", "Solo shoots", "My production")
    var titleTextToUse: String = pageTitleTexts[currentPageIndex]
    if (currentPageIndex == SelectionPages.PRODUCTION_SHOOTS.ordinal) {
        titleTextToUse = productionSelectionUIState.selectedProduction?.name ?: "My production"
    }

    Box() {
        if(currentPageIndex != SelectionPages.PRODUCTION_SHOOTS.ordinal) {
            AboutButton(
                modifier,
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.secondary,
                goToAboutScreen
            )
        }

        Column(modifier.fillMaxWidth()) {
            if (currentPageIndex == SelectionPages.PRODUCTION_SHOOTS.ordinal) {
                GoBackEditDeleteBar(
                    modifier,
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.background,
                    { productionSelectionViewModel.goOutOfProduction() },
                    { /* TODO */ },
                    { productionSelectionViewModel.deleteProduction() })
            }
            else {
                Spacer(modifier = modifier.size(30.dp))
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
            OutlinedTextField(
                value = "",
                placeholder = { Text("Search...", color = MaterialTheme.colorScheme.primary, fontSize = 18.sp) },
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
            AddNewOrderByButtons(
                modifier = modifier,
                currentPageIndex = currentPageIndex,
                productionSelectionViewModel= productionSelectionViewModel,
                productionSelectionUIState = productionSelectionUIState,
                navigateToCreateShootScreen = navigateToCreateShootScreen,
            )
        }
    }
}
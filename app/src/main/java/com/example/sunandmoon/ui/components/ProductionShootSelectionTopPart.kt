package com.example.sunandmoon.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sunandmoon.R
import com.example.sunandmoon.data.ShootSelectionUIState
import com.example.sunandmoon.ui.components.buttonComponents.AboutButton
import com.example.sunandmoon.ui.components.buttonComponents.AddNewOrderByButtons
import com.example.sunandmoon.ui.components.buttonComponents.GoBackEditDeleteBar
import com.example.sunandmoon.ui.components.buttonComponents.PagePickerProductionsShoots
import com.example.sunandmoon.viewModel.SelectionPages
import com.example.sunandmoon.viewModel.ShootSelectionViewModel

//top-bar used in production selection and shoot selection. user can search, go back, and create stuff
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductionShootSelectionTopPart(
    modifier: Modifier,
    navigateToCreateShootScreen: (parentProductionId: Int?, shootToEditId: Int?) -> Unit,
    shootSelectionViewModel: ShootSelectionViewModel = hiltViewModel(),
    currentPageIndex: Int,
    shootSelectionUIState: ShootSelectionUIState,
    goToAboutScreen: () -> Unit
) {
    val pageTitleTexts = listOf(R.string.YourProductions, R.string.Solos_shoots, R.string.defaultProductionName)
    var titleTextToUse: String = stringResource(id = pageTitleTexts[currentPageIndex])
    if (currentPageIndex == SelectionPages.PRODUCTION_SHOOTS.ordinal) {
        titleTextToUse = shootSelectionUIState.selectedProduction?.name ?: stringResource(id = R.string.defaultProductionName)
    }

    Box {
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
                    MaterialTheme.colorScheme.secondary,
                    { shootSelectionViewModel.goOutOfProduction() },
                    {
                        shootSelectionViewModel.setProductionName(shootSelectionUIState.selectedProduction?.name)
                    },
                    { shootSelectionViewModel.deleteProduction() })
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
            TextField(
                value = shootSelectionUIState.searchQuery,
                placeholder = { Text(stringResource(id = R.string.Search), color = MaterialTheme.colorScheme.primary, fontSize = 18.sp) },
                onValueChange = { query ->
                    shootSelectionViewModel.setSearchQuery(query)
                },
                singleLine = true,
                modifier = modifier
                    .fillMaxWidth(0.8f)
                    .align(Alignment.CenterHorizontally),
                leadingIcon = {
                    Icon(
                        painterResource(R.drawable.search),
                        stringResource(id = R.string.LocationSearchFieldIcon),
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
                PagePickerProductionsShoots(modifier, shootSelectionViewModel, currentPageIndex)
            }
            AddNewOrderByButtons(
                modifier = modifier,
                currentPageIndex = currentPageIndex,
                shootSelectionViewModel= shootSelectionViewModel,
                shootSelectionUIState = shootSelectionUIState,
                navigateToCreateShootScreen = navigateToCreateShootScreen,
            )
        }
    }
}
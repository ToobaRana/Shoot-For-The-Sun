package com.example.sunandmoon.ui.components.buttonComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sunandmoon.R
import com.example.sunandmoon.data.ShootSelectionUIState
import com.example.sunandmoon.viewModel.SelectionPages
import com.example.sunandmoon.viewModel.ShootSelectionViewModel

//button for adding new production/shoot or ordering them
@Composable
fun AddNewOrderByButtons(
    modifier: Modifier,
    currentPageIndex: Int,
    shootSelectionViewModel: ShootSelectionViewModel = hiltViewModel(),
    shootSelectionUIState: ShootSelectionUIState,
    navigateToCreateShootScreen: (parentProductionId: Int?, shootToEditId: Int?) -> Unit,
    ) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(20.dp, 0.dp, 20.dp, 10.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier
                .fillMaxWidth()
                .padding(0.dp, 20.dp)
                .height(40.dp),
            horizontalArrangement = Arrangement.Start
        ) {

            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(15.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable {
                        if (currentPageIndex == SelectionPages.PRODUCTIONS.ordinal) {
                            shootSelectionViewModel.setProductionName("")
                        } else {
                            navigateToCreateShootScreen(
                                shootSelectionUIState.selectedProduction?.id,
                                null
                            )
                        }
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painterResource(id = R.drawable.plus),
                    contentDescription = stringResource(R.string.AddNew),
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = modifier
                        .padding(7.dp)
                        .padding(start = 6.dp)
                )
                Text(
                    text = stringResource(R.string.AddNew),
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.End,
                    modifier = modifier.padding(end = 17.dp)
                )
            }

            Spacer(modifier = modifier.weight(0.2f))

            Box(modifier = modifier.weight(1f)) {
                OrderByDropdown(modifier, shootSelectionUIState)
            }
        }
    }
}
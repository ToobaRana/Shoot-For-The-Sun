package com.example.sunandmoon.ui.components.buttonComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sunandmoon.R
import com.example.sunandmoon.data.ProductionSelectionUIState
import com.example.sunandmoon.viewModel.ProductionSelectionViewModel
import com.example.sunandmoon.viewModel.SelectionPages

@Composable
fun AddNewOrderByButtons(
    modifier: Modifier,
    currentPageIndex: Int,
    productionSelectionViewModel: ProductionSelectionViewModel = hiltViewModel(),
    productionSelectionUIState: ProductionSelectionUIState,
    navigateToCreateShootScreen: (parentProductionId: Int?, shootToEditId: Int?) -> Unit,

    ) {
    Box(
        modifier
            .fillMaxWidth()
            .padding(20.dp, 20.dp, 20.dp, 10.dp), contentAlignment = Alignment.Center
    ) {
        Row(
            modifier
                .fillMaxWidth(0.9f)
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
                            productionSelectionViewModel.setProductionName("")
                        } else {
                            navigateToCreateShootScreen(
                                productionSelectionUIState.selectedProduction?.id,
                                null
                            )
                        }
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painterResource(id = R.drawable.plus),
                    contentDescription = "Add New",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .padding(7.dp)
                        .padding(start = 6.dp)
                )
                Text(
                    text = "Add new",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.End,
                    modifier = modifier.padding(end = 17.dp)
                )
            }

            Spacer(modifier = modifier.weight(0.2f))

            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(15.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable {
                        /* TODO */
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painterResource(id = R.drawable.sortdown),
                    contentDescription = "Order by",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(35.dp)
                        .padding(start = 15.dp, top = 3.dp)
                )
                Text(
                    text = "Order by",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.End,
                    modifier = modifier.padding(end = 22.dp)
                )
            }
        }
    }
}
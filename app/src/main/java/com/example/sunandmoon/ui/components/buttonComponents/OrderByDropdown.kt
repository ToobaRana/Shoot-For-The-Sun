package com.example.sunandmoon.ui.components.buttonComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sunandmoon.R
import com.example.sunandmoon.data.ShootSelectionUIState
import com.example.sunandmoon.data.localDatabase.dao.ProductionOrderBy
import com.example.sunandmoon.data.localDatabase.dao.ShootOrderBy
import com.example.sunandmoon.viewModel.SelectionPages
import com.example.sunandmoon.viewModel.ShootSelectionViewModel

//shows order by dropdown for sorting
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderByDropdown(modifier: Modifier, shootSelectionUIState: ShootSelectionUIState, shootSelectionViewModel: ShootSelectionViewModel = hiltViewModel()) {
    ExposedDropdownMenuBox(
        expanded = shootSelectionUIState.orderByDropdownOpened,
        onExpandedChange = { shootSelectionViewModel.openCloseOrderByDropdown() },
        modifier = modifier.fillMaxWidth()
    ) {

        Row(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clip(RoundedCornerShape(15.dp))
                .background(MaterialTheme.colorScheme.primary)
                .menuAnchor(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painterResource(id = R.drawable.sortdown),
                contentDescription = stringResource(R.string.OrderBy),
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = modifier
                    .size(35.dp)
                    .padding(start = 15.dp, top = 3.dp)
            )
            Text(
                text =  stringResource(R.string.OrderBy),
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.End,
                modifier = modifier.padding(end = 22.dp)
            )
        }

        ExposedDropdownMenu(
            expanded = shootSelectionUIState.orderByDropdownOpened,
            onDismissRequest = { shootSelectionViewModel.openCloseOrderByDropdown()},
            modifier = modifier.background(MaterialTheme.colorScheme.secondary)
            //modifier = modifier.background(MaterialTheme.colorScheme.tertiary)
        ) {

            if(shootSelectionUIState.currentPageIndex == SelectionPages.PRODUCTIONS.ordinal) {
                DropdownMenuItem(
                    onClick = {
                        shootSelectionViewModel.setProductionOrderBy(ProductionOrderBy.START_DATE_TIME)
                        shootSelectionViewModel.openCloseOrderByDropdown()
                    },
                    text = { Text(stringResource(id = R.string.StartDate), color = MaterialTheme.colorScheme.primary, fontFamily = FontFamily(Font(R.font.nunito_bold))) },
                )
                DropdownMenuItem(
                    onClick = {
                        shootSelectionViewModel.setProductionOrderBy(ProductionOrderBy.END_DATE_TIME)
                        shootSelectionViewModel.openCloseOrderByDropdown()
                    },
                    text = { Text(stringResource(id = R.string.EndDate), color = MaterialTheme.colorScheme.primary, fontFamily = FontFamily(Font(R.font.nunito_bold))) }
                )
                DropdownMenuItem(
                    onClick = {
                        shootSelectionViewModel.setProductionOrderBy(ProductionOrderBy.NAME)
                        shootSelectionViewModel.openCloseOrderByDropdown()
                    },
                    text = { Text(stringResource(id = R.string.Name), color = MaterialTheme.colorScheme.primary, fontFamily = FontFamily(Font(R.font.nunito_bold))) }
                )
            }
            else {
                DropdownMenuItem(
                    onClick = {
                        shootSelectionViewModel.setShootOrderBy(ShootOrderBy.DATE_TIME)
                        shootSelectionViewModel.openCloseOrderByDropdown()
                    },
                    text = { Text(stringResource(id = R.string.StartDate), color = MaterialTheme.colorScheme.primary, fontFamily = FontFamily(Font(R.font.nunito_bold))) },
                )
                DropdownMenuItem(
                    onClick = {
                        shootSelectionViewModel.setShootOrderBy(ShootOrderBy.NAME)
                        shootSelectionViewModel.openCloseOrderByDropdown()
                    },
                    text = { Text(stringResource(id = R.string.Name), color = MaterialTheme.colorScheme.primary, fontFamily = FontFamily(Font(R.font.nunito_bold))) }
                )
                DropdownMenuItem(
                    onClick = {
                        shootSelectionViewModel.setShootOrderBy(ShootOrderBy.LATITUDE)
                        shootSelectionViewModel.openCloseOrderByDropdown()
                    },
                    text = { Text(stringResource(id = R.string.Latitude), color = MaterialTheme.colorScheme.primary, fontFamily = FontFamily(Font(R.font.nunito_bold))) }
                )
                DropdownMenuItem(
                    onClick = {
                        shootSelectionViewModel.setShootOrderBy(ShootOrderBy.LONGITUDE)
                        shootSelectionViewModel.openCloseOrderByDropdown()
                    },
                    text = { Text(stringResource(id = R.string.Longitude), color = MaterialTheme.colorScheme.primary, fontFamily = FontFamily(Font(R.font.nunito_bold))) }
                )
            }
        }
    }
}

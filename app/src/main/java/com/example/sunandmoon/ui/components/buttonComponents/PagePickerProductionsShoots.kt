package com.example.sunandmoon.ui.components.buttonComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sunandmoon.viewModel.ShootSelectionViewModel
import com.example.sunandmoon.viewModel.SelectionPages

//used to switch between showing individual shoots and productions in main screen
@Composable
fun PagePickerProductionsShoots(
    modifier: Modifier,
    shootSelectionViewModel: ShootSelectionViewModel = viewModel(),
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
                .fillMaxWidth()
                .height(35.dp)
                .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(15.dp)),
            horizontalArrangement = Arrangement.Start
        ) {
            val colors =
                listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary)
            Button(
                onClick = { if (currentPageIndex != SelectionPages.PRODUCTIONS.ordinal) shootSelectionViewModel.changeCurrentPageIndex() },
                modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = colors[currentPageIndex]),
                contentPadding = PaddingValues(vertical = 4.dp),
                shape = RoundedCornerShape(15.dp)
            ) {
                Text(stringResource(com.example.sunandmoon.R.string.Productions), color = colors[(currentPageIndex + 1) % 2], fontSize = 18.sp)
            }
            Button(
                onClick = { if (currentPageIndex != SelectionPages.SHOOTS.ordinal) shootSelectionViewModel.changeCurrentPageIndex() },
                modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = colors[(currentPageIndex + 1) % 2]),
                contentPadding = PaddingValues(vertical = 4.dp),
                shape = RoundedCornerShape(15.dp)
            ) {
                Text(stringResource(com.example.sunandmoon.R.string.SoloShoots), color = colors[currentPageIndex], fontSize = 18.sp)
            }
        }
    }
}
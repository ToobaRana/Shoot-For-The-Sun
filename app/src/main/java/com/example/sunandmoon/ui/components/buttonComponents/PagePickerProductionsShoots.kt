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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sunandmoon.viewModel.ProductionSelectionViewModel
import com.example.sunandmoon.viewModel.SelectionPages

@Composable
fun PagePickerProductionsShoots(
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
                Text("Solo shoots", color = colors[currentPageIndex], fontSize = 14.sp)
            }
        }
    }
}
package com.example.sunandmoon.ui.components.infoComponents

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.sunandmoon.data.ProductionSelectionUIState
import com.example.sunandmoon.viewModel.ProductionSelectionViewModel

@Composable
fun PreferredWeatherDialog(productionSelectionViewModel: ProductionSelectionViewModel, productionSelectionUIState: ProductionSelectionUIState) {
    AlertDialog(
        onDismissRequest = {
            productionSelectionViewModel.setShowPreferredWeatherDialog(false)
        },
        title = { Text(text = "Variable Changed") },
        text = { Text(text = "The preferred weather on your shoot is updated") },
        confirmButton = {
            Button(onClick = { productionSelectionViewModel.setShowPreferredWeatherDialog(false) }) {
                Text(text = "OK")
            }
        }
    )
}


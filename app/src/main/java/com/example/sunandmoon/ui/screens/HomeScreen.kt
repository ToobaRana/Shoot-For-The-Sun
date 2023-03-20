package com.example.sunandmoon.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sunandmoon.viewModel.SunViewModel

@Composable
fun HomeScreen(sunViewModel: SunViewModel = viewModel()){


    val sunUiState by sunViewModel.sunUiState.collectAsState()

}
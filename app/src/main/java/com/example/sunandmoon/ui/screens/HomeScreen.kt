package com.example.sunandmoon.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sunandmoon.viewModel.SunViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(sunViewModel: SunViewModel = viewModel()){


    val sunUiState by sunViewModel.sunUiState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {



        },


        content = {innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(top = 20.dp)
            )

            {


                /*items(alpacaPartyList){alpacaParty ->

                    val totalVotes = alpacaViewModel.totalVotes()

                    AlpacaCard(
                        alpacaParty,
                        checkDistrict(chosenDistrict, votesUiState, alpacaParty), totalVotes, votesUiState)
                }*/

            }


        },
        bottomBar = {


        }
    )

}
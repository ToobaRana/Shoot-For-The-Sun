package com.example.sunandmoon.ui.components.infoComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sunandmoon.R
import com.example.sunandmoon.ui.components.CalendarComponent
import com.example.sunandmoon.ui.components.NavigationComposable
import com.example.sunandmoon.ui.components.TableCard
import com.example.sunandmoon.ui.components.userInputComponents.dropdownMenuSunType
import com.example.sunandmoon.ui.screens.LocationSearch
import com.example.sunandmoon.viewModel.TableViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TableMissingInternet(
    modifier: Modifier,
    navigateToNextBottomBar: (index: Int) -> Unit,
    tableViewModel: TableViewModel = viewModel()
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 20.dp),
        topBar = {

            Text(
                text = stringResource(R.string.Comparing),
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp, start = 25.dp, end = 25.dp),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight(400),
                minLines = 2,
                lineHeight = 35.sp
            )
        },

        content = { innerPadding ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = modifier
                        .clip(RoundedCornerShape(15.dp))
                        .background(MaterialTheme.colorScheme.secondary)
                        .padding(25.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.NoInternet),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = modifier,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = modifier.size(20.dp))
                    Button(onClick = {
                        tableViewModel.loadTableSunInformation()
                    }) {
                        Text(text = "Try Again")
                    }
                }
                Spacer(modifier = modifier.size(80.dp))
            }
        },
        bottomBar = {
            NavigationComposable(modifier = modifier, page = 2, navigateToNextBottomBar)
        }
    )
}
package com.example.sunandmoon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sunandmoon.ui.screens.HomeScreen
import com.example.sunandmoon.ui.screens.TableScreen
import com.example.sunandmoon.ui.screens.TableView
import com.example.sunandmoon.ui.theme.SunAndMoonTheme
import com.example.sunandmoon.viewModel.SunViewModel
import com.example.sunandmoon.viewModel.TableViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SunAndMoonTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val sunViewModel = SunViewModel()
                    val tableViewModel = TableViewModel()

                    MultipleScreenNavigator(sunViewModel, tableViewModel)
                }
            }
        }
    }
}

@Composable
fun MultipleScreenNavigator(sunViewModel : SunViewModel, tableViewModel: TableViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "homescreen") {
        composable("homescreen") { HomeScreen(modifier = Modifier.fillMaxSize(), navigateToNext = {navController.navigate("tablescreen")}, sunViewModel)}
        composable("tablescreen"){ TableScreen(modifier = Modifier.fillMaxSize(), tableViewModel)}
    }
}
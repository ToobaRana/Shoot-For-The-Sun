package com.example.sunandmoon

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.NavController
import com.example.sunandmoon.navigation.MultipleScreenNavigator
import com.example.sunandmoon.ui.screens.ShootSelectionScreen
import com.example.sunandmoon.ui.theme.SunAndMoonTheme
import com.example.sunandmoon.viewModel.ShootSelectionViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
class ShootInfoScreenKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testShootInfoScreen() {
        val mockNavController = mockk<NavController>()
        val mockViewModel = mockk<ShootSelectionViewModel>()
        val mockCurrentScreenRoute = mockk<String>()
        val mockGoToAboutScreen: () -> Unit = mockk()

        every { mockGoToAboutScreen.invoke() } answers {
            // Perform any desired actions inside the mock function
            println("Mock function called")
        }

        composeTestRule.setContent {
            ShootSelectionScreen(
                modifier = Modifier,
                navigateToShootInfoScreen = { shootId -> /* handle navigation */ },
                shootSelectionViewModel = mockViewModel,
                navigateToNextBottomBar = { index -> /* handle navigation */ },
                navigateToCreateShootScreen = { parentProductionId, shootToEditId -> /* handle navigation */ },
                navController = mockNavController,
                currentScreenRoute = mockCurrentScreenRoute,
                goToAboutScreen = mockGoToAboutScreen
            )
        }

        verify { mockGoToAboutScreen.invoke() }
    }
}
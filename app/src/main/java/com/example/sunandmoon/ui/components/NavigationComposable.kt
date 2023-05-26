package com.example.sunandmoon.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.sunandmoon.R

//used for navigation between screens
@Composable
fun NavigationComposable(modifier: Modifier, page: Int, navigateToNextBottomBar: (index: Int) -> Unit){
    val icons = listOf(R.drawable.home, R.drawable.ar, R.drawable.table)
    val iconsContentDescriptions: List<String> = listOf(stringResource(id = R.string.HomeButton), stringResource(id = R.string.ARButton), stringResource(id = R.string.TableButton)) // We wrote "A R" with space between to make the text-to-speech more understandable

    NavigationBar(containerColor = MaterialTheme.colorScheme.tertiary, modifier = modifier.height(75.dp)) {
        icons.forEachIndexed { index, icon ->
            //val iconColor = if (page == index) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
            NavigationBarItem(
                icon = { Icon(painterResource(icon), iconsContentDescriptions[index]) },
                selected = page == index,
                onClick = {
                    if(index != page){
                        navigateToNextBottomBar(index)
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.secondary,
                    indicatorColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.primary,
                )
            )
        }
    }
}

package com.example.sunandmoon.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*

@Composable
fun NavigationComposable(page: Int, navigateToNext: () -> Unit){
    val items = listOf("Home", "Table")
    val icons = listOf(Icons.Filled.Star, Icons.Filled.Menu)

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(icons[index], contentDescription = item) },
                label = { Text(item) },
                selected = page == index,
                onClick = {
                    if(index != page){
                        navigateToNext()
                    }
                }
            )
        }
    }
}

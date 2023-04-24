package com.example.sunandmoon.ui.components

import android.location.Location
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.sunandmoon.R
import com.example.sunandmoon.data.util.Shoot
import java.time.LocalDateTime

@Composable
fun NavigationComposable(page: Int, navigateToNextBottomBar: (index: Int) -> Unit){
    //val items = listOf("Home", "Table")

    val icons = listOf(R.drawable.home_icon, R.drawable.find_shoot_icon, R.drawable.table_icon)
    val iconsContentDescriptions = listOf("home button", "find shoot button", "table button")

    NavigationBar(containerColor = MaterialTheme.colorScheme.tertiary) {
        icons.forEachIndexed { index, icon ->
            val iconColor = if (page == index)  MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
            NavigationBarItem(
                icon = { Icon(painterResource(icon), iconsContentDescriptions[index], Modifier, iconColor) },
                //label = { Text(item) },
                selected = page == index,
                onClick = {
                    if(index != page){
                        navigateToNextBottomBar(index)
                    }
                }
            )
        }
    }
}

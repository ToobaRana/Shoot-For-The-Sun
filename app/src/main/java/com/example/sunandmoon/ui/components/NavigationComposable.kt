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
import com.example.sunandmoon.ui.theme.InfoBlueColor
import java.time.LocalDateTime

@Composable
fun NavigationComposable(page: Int, navigateToNextBottomBar: (index: Int) -> Unit){
    val icons = listOf(R.drawable.home, R.drawable.ar, R.drawable.table)
    val iconsContentDescriptions = listOf("home button", "A R button", "table button") // We wrote "A R" with space between to make the text-to-speech more understandable

    NavigationBar(containerColor = MaterialTheme.colorScheme.tertiary) {
        icons.forEachIndexed { index, icon ->
            val iconColor = if (page == index) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
            NavigationBarItem(
                icon = { Icon(painterResource(icon), iconsContentDescriptions[index], Modifier, iconColor) },
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

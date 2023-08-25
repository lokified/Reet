package com.loki.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.loki.ui.components.AppBottomBar
import com.loki.ui.components.AppBottomBarItem

@Composable
fun BottomNavigation(
    screens: List<Screen>,
    onNavigateTo: (Screen) -> Unit,
    currentDestination: NavDestination?,
    isVisible: Boolean,
    isDarkTheme: Boolean
) {

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {

        AppBottomBar(
            containerColor = if (!isDarkTheme) MaterialTheme.colorScheme.background
            else MaterialTheme.colorScheme.primary
        ) {

            screens.forEach { screen ->

                val selected = currentDestination?.hierarchy?.any { it.route == screen.route } ?: false

                AppBottomBarItem(
                    selected = selected,
                    onClick = { onNavigateTo(screen) },
                    icon = {
                        Icon(
                            imageVector = screen.icon ?: Icons.Filled.Warning,
                            contentDescription = null
                        )
                    },
                    label = { Text(text = screen.title ?: "") }
                )
            }
        }
    }
}
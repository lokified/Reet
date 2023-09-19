package com.loki.camera.util

import android.app.Activity
import android.view.View
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

object StatusBarUtil {

    @Composable
    fun defaultStatusBarColor() = MaterialTheme.colorScheme.background.toArgb()

    @Composable
    fun defaultNavColor(darkTheme: Boolean) =
        if (darkTheme) MaterialTheme.colorScheme.primary.toArgb()
         else MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp).toArgb()

    @Composable
    fun DefaultStatusColors(
        view: View,
        isDark: Boolean,
        statusBarColor: Int = defaultStatusBarColor(),
        navigationBarColor: Int = defaultNavColor(isDark),
    ) {

        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = statusBarColor
            window.navigationBarColor = navigationBarColor
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = isDark
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = isDark
        }
    }
}
package com.loki.navigation.ext

import androidx.navigation.NavController
import com.loki.navigation.Screen

fun NavController.navigateTo(
    screen: Screen
) {

    val currentRoute: String? = this.currentBackStackEntry?.destination?.route

    val route = screen.routePath?.let { routePath ->
        screen.route + "/$routePath"
    } ?: screen.route

    navigate(route) {
        if (screen.icon != null) {
            popUpTo(Screen.ReportListScreen.route) {
                saveState = true
            }
        }
        launchSingleTop = true
        restoreState = screen.restoreState

        //clear back stack if required
        if (screen.clearBackStack && !currentRoute.isNullOrEmpty()) {
            popUpTo(currentRoute) {
                inclusive = true
            }
        }
    }
}

fun NavController.clearAndRestart(
    screen: Screen
) {

    navigate(screen.route) {
        launchSingleTop = true
        popUpTo(0) {
            inclusive = true
        }
    }
}
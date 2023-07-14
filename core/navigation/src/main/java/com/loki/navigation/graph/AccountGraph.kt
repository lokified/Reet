package com.loki.navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.loki.navigation.NavigationViewModel
import com.loki.navigation.Screen
import com.loki.navigation.ext.navigateTo
import com.loki.navigation.profileScreen
import com.loki.navigation.settingsScreen

@Composable
fun AccountNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: NavigationViewModel
) {

    NavHost(
        navController = navController,
        startDestination = Screen.ProfileScreen.route,
        modifier = modifier
    ) {
        profileScreen(onNavigateTo = navController::navigateTo, viewModel = viewModel)
        settingsScreen(onNavigateBack = navController::navigateUp, viewModel = viewModel)
    }
}
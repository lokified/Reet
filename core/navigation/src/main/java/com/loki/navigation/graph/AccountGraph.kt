package com.loki.navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.loki.navigation.NavigationViewModel
import com.loki.navigation.Screen
import com.loki.navigation.cameraScreen
import com.loki.navigation.ext.navigateTo
import com.loki.navigation.profileScreen
import com.loki.navigation.settingsScreen
import com.loki.navigation.usernameChangeScreen
import com.loki.navigation.videoPlayerScreen

@Composable
fun AccountNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: NavigationViewModel,
    onNavigateToLogin: (Screen) -> Unit
) {

    NavHost(
        navController = navController,
        startDestination = Screen.ProfileScreen.route,
        modifier = modifier
    ) {
        profileScreen(onNavigateTo = navController::navigateTo, viewModel = viewModel, onNavigateToLogin = onNavigateToLogin)
        usernameChangeScreen(onNavigateBack = navController::navigateUp, viewModel = viewModel)
        settingsScreen(onNavigateBack = navController::navigateUp, viewModel = viewModel)
        cameraScreen(onNavigateBack = navController::navigateUp, onNavigateTo = navController::navigateTo, viewModel = viewModel)
        videoPlayerScreen(viewModel = viewModel)
    }
}
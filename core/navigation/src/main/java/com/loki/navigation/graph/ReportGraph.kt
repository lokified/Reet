package com.loki.navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.loki.navigation.NavigationViewModel
import com.loki.navigation.Screen
import com.loki.navigation.ext.navigateTo
import com.loki.navigation.newReportScreen
import com.loki.navigation.reportListScreen
import com.loki.navigation.reportScreen

@Composable
fun ReportNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: NavigationViewModel
) {

    NavHost(
        navController = navController,
        startDestination = Screen.ReportListScreen.route,
        modifier = modifier
    ) {

        reportListScreen(onNavigateTo = navController::navigateTo, viewModel = viewModel)
        reportScreen(onNavigateBack = navController::navigateUp, viewModel = viewModel)
        newReportScreen(onNavigateTo = navController::navigateTo, viewModel = viewModel)
    }
}
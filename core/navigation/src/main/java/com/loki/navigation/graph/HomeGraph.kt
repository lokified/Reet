package com.loki.navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.loki.navigation.NavigationViewModel
import com.loki.navigation.Screen
import com.loki.navigation.accountNavGraph
import com.loki.navigation.newsScreen
import com.loki.navigation.reportNavGraph

@Composable
fun HomeNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onNavigateToLogin: (Screen) -> Unit,
    viewModel: NavigationViewModel
) {

    NavHost(
        navController = navController,
        startDestination = Screen.ReportListScreen.route,
        modifier = modifier
    ) {

        reportNavGraph(viewModel)
        newsScreen()
        accountNavGraph(viewModel, onNavigateToLogin)
    }
}
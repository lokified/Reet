package com.loki.navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.loki.navigation.Screen
import com.loki.navigation.ext.navigateTo
import com.loki.navigation.homeNavGraph
import com.loki.navigation.loginScreen
import com.loki.navigation.registerScreen

@Composable
fun RootNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: Screen
) {

    NavHost(
        navController = navController,
        startDestination = startDestination.route,
        modifier = modifier
    ) {

        loginScreen(navigateTo = navController::navigateTo)
        registerScreen(navigateTo = navController::navigateTo)
        homeNavGraph(onNavigateToRoot = navController::navigateTo)
    }
}
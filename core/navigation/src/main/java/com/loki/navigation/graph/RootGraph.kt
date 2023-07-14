package com.loki.navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.loki.navigation.Screen
import com.loki.navigation.ext.clearAndRestart
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
    val logoutEvent = rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(logoutEvent.value) {
        if (logoutEvent.value) {
            navController.popBackStack(navController.graph.startDestinationId, inclusive = true)
            navController.clearAndRestart(Screen.LoginScreen)
            logoutEvent.value = false
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination.route,
        modifier = modifier
    ) {


        loginScreen(navigateTo = navController::navigateTo)
        registerScreen(navigateTo = navController::navigateTo)
        homeNavGraph(
            onNavigateToLogin = {
                logoutEvent.value = true
            }
        )
    }
}
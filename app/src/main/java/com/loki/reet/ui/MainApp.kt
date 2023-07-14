package com.loki.reet.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.loki.navigation.Screen
import com.loki.navigation.graph.RootNavGraph

@Composable
fun MainApp() {

    val navController = rememberNavController()

    RootNavGraph(
        navController = navController,
        startDestination = Screen.LoginScreen
    )
}
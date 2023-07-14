package com.loki.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.loki.auth.login.LoginScreen
import com.loki.auth.login.LoginViewModel
import com.loki.auth.register.RegisterScreen
import com.loki.auth.register.RegisterViewModel
import com.loki.home.home.HomeScreen
import com.loki.home.report_list.ReportListScreen
import com.loki.navigation.ext.navigateTo
import com.loki.navigation.graph.AccountNavGraph
import com.loki.navigation.graph.HomeNavGraph
import com.loki.navigation.graph.ReportNavGraph
import com.loki.new_report.NewReportScreen
import com.loki.news.NewsScreen
import com.loki.profile.ProfileScreen
import com.loki.profile.ProfileViewModel
import com.loki.report.ReportScreen
import com.loki.settings.SettingsScreen

fun NavGraphBuilder.homeNavGraph(onNavigateToLogin: (Screen) -> Unit) {

    composable(route = Screen.HomeScreen.route) {

        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val viewModel = NavigationViewModel()

        val bottomBar: @Composable () -> Unit = {

            BottomNavigation(
                screens = listOf(
                    Screen.ReportListScreen,
                    Screen.NewsScreen,
                    Screen.ProfileScreen
                ),
                onNavigateTo = navController::navigateTo,
                currentDestination = navBackStackEntry?.destination,
                isVisible = viewModel.isBottomBarVisible.value
            )
        }

        val nestedGraph: @Composable () -> Unit = {
            HomeNavGraph(
                navController = navController,
                onNavigateToLogin = onNavigateToLogin,
                viewModel = viewModel
            )
        }

        HomeScreen(
            nestedNavGraph = nestedGraph,
            bottomBar = bottomBar
        )
    }
}


fun NavGraphBuilder.loginScreen(navigateTo: (Screen) -> Unit) {
    composable(route = Screen.LoginScreen.route) {
        val viewModel = hiltViewModel<LoginViewModel>()
        LoginScreen(
            viewModel = viewModel,
            navigateToRegister = { navigateTo(Screen.RegisterScreen) },
            navigateToHome = { navigateTo(Screen.HomeScreen.withClearBackStack()) }
        )
    }
}

fun NavGraphBuilder.registerScreen(navigateTo: (Screen) -> Unit) {
    composable(route = Screen.RegisterScreen.route) {
        val viewModel = hiltViewModel<RegisterViewModel>()
        RegisterScreen(
            viewModel = viewModel,
            navigateToLogin = { navigateTo(Screen.LoginScreen) }
        )
    }
}


fun NavGraphBuilder.reportNavGraph(viewModel: NavigationViewModel) {
    composable(route = Screen.ReportListScreen.route) {
        val navController = rememberNavController()
        ReportNavGraph(navController = navController, viewModel = viewModel)
    }
}

fun NavGraphBuilder.reportListScreen(onNavigateTo: (Screen) -> Unit, viewModel: NavigationViewModel) {
    composable(route = Screen.ReportListScreen.route) {
        LaunchedEffect(key1 = viewModel.isBottomBarVisible.value) {
            viewModel.setBottomBarVisible(true)
        }
        ReportListScreen(
            navigateToNewReport = {
                onNavigateTo(Screen.NewReportScreen)
            },
            navigateToReport = {
                onNavigateTo(Screen.ReportScreen)
            }
        )
    }
}

fun NavGraphBuilder.newReportScreen(onNavigateTo: (Screen) -> Unit, viewModel: NavigationViewModel) {
    composable(route = Screen.NewReportScreen.route) {
        LaunchedEffect(key1 = viewModel.isBottomBarVisible.value) {
            viewModel.setBottomBarVisible(false)
        }
        NewReportScreen()
    }
}

fun NavGraphBuilder.reportScreen(onNavigateBack: () -> Unit, viewModel: NavigationViewModel) {
    composable(route = Screen.ReportScreen.route) {
        LaunchedEffect(key1 = viewModel.isBottomBarVisible.value) {
            viewModel.setBottomBarVisible(false)
        }
        ReportScreen(onNavigateBack = onNavigateBack)
    }
}

fun NavGraphBuilder.newsScreen() {
    composable(route = Screen.NewsScreen.route) {
        NewsScreen()
    }
}

fun NavGraphBuilder.accountNavGraph(viewModel: NavigationViewModel, onNavigateToLogin: (Screen) -> Unit) {
    composable(route = Screen.ProfileScreen.route) {
        val navController = rememberNavController()
        AccountNavGraph(
            navController = navController,
            viewModel = viewModel,
            onNavigateToLogin = onNavigateToLogin
        )
    }
}

fun NavGraphBuilder.profileScreen(onNavigateTo: (Screen) -> Unit, onNavigateToLogin: (Screen) -> Unit, viewModel: NavigationViewModel) {
    composable(route = Screen.ProfileScreen.route) {
        LaunchedEffect(key1 = viewModel.isBottomBarVisible.value) {
            viewModel.setBottomBarVisible(true)
        }

        val profileViewModel = hiltViewModel<ProfileViewModel>()
        ProfileScreen(
            viewModel = profileViewModel,
            navigateToSettings = { onNavigateTo(Screen.SettingsScreen) },
            navigateToLogin = { onNavigateToLogin(Screen.LoginScreen) }
        )
    }
}

fun NavGraphBuilder.settingsScreen(onNavigateBack: () -> Unit, viewModel: NavigationViewModel) {
    composable(route = Screen.SettingsScreen.route) {
        LaunchedEffect(key1 = viewModel.isBottomBarVisible.value) {
            viewModel.setBottomBarVisible(false)
        }
        SettingsScreen(
            navigateBack = onNavigateBack
        )
    }
}
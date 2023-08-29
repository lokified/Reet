package com.loki.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.loki.auth.forgotPassword.ForgotPasswordScreen
import com.loki.auth.forgotPassword.ForgotPasswordViewModel
import com.loki.auth.login.LoginScreen
import com.loki.auth.login.LoginViewModel
import com.loki.auth.register.RegisterScreen
import com.loki.auth.register.RegisterViewModel
import com.loki.home.home.HomeScreen
import com.loki.home.report_list.ReportListScreen
import com.loki.home.report_list.ReportListViewModel
import com.loki.navigation.ext.navigateTo
import com.loki.navigation.graph.AccountNavGraph
import com.loki.navigation.graph.HomeNavGraph
import com.loki.navigation.graph.ReportNavGraph
import com.loki.new_report.NewReportScreen
import com.loki.new_report.NewReportViewModel
import com.loki.news.NewsScreen
import com.loki.news.NewsViewModel
import com.loki.profile.profile.ProfileScreen
import com.loki.profile.ProfileViewModel
import com.loki.profile.username.UsernameChangeScreen
import com.loki.report.ReportScreen
import com.loki.report.ReportViewModel
import com.loki.settings.SettingsScreen
import com.loki.settings.SettingsViewModel
import com.loki.ui.utils.Constants.REPORT_ID
import kotlinx.coroutines.delay

fun NavGraphBuilder.homeNavGraph(viewModel: NavigationViewModel,onNavigateToLogin: (Screen) -> Unit) {

    composable(route = Screen.HomeScreen.route) {

        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val settingsViewModel = hiltViewModel<SettingsViewModel>()

        val bottomBar: @Composable () -> Unit = {

            BottomNavigation(
                screens = listOf(
                    Screen.ReportListScreen,
                    Screen.NewsScreen,
                    Screen.ProfileScreen
                ),
                onNavigateTo = navController::navigateTo,
                currentDestination = navBackStackEntry?.destination,
                isVisible = viewModel.isBottomBarVisible.value,
                isDarkTheme = settingsViewModel.isDarkTheme.value
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


fun NavGraphBuilder.loginScreen(viewModel: NavigationViewModel, navigateTo: (Screen) -> Unit) {

    composable(route = Screen.LoginScreen.route) {
        val loginViewModel = hiltViewModel<LoginViewModel>()
        LaunchedEffect(key1 = Unit) {
            loginViewModel.onAppStart {
                navigateTo(Screen.HomeScreen.withClearBackStack())
            }
            delay(3000L)
            viewModel.isLoggingIn.value = false
        }

        if (!viewModel.isLoggingIn.value) {
            LoginScreen(
                viewModel = loginViewModel,
                navigateToRegister = { navigateTo(Screen.RegisterScreen) },
                navigateToForgotScreen = { navigateTo(Screen.ForgotPasswordScreen) },
                navigateToHome = { navigateTo(Screen.HomeScreen.withClearBackStack()) }
            )
        }
    }
}

fun NavGraphBuilder.registerScreen(navigateBack: () -> Unit) {
    composable(route = Screen.RegisterScreen.route) {
        val viewModel = hiltViewModel<RegisterViewModel>()
        RegisterScreen(
            viewModel = viewModel,
            navigateToLogin = navigateBack
        )
    }
}

fun NavGraphBuilder.forgotPasswordScreen(navigateBack: () -> Unit) {
    composable(route = Screen.ForgotPasswordScreen.route) {
        val viewModel = hiltViewModel<ForgotPasswordViewModel>()
        ForgotPasswordScreen(
            viewModel = viewModel,
            navigateBack = navigateBack
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

        val reportListViewModel = hiltViewModel<ReportListViewModel>()

        ReportListScreen(
            viewModel = reportListViewModel,
            navigateToNewReport = {
                onNavigateTo(Screen.NewReportScreen)
            },
            navigateToReport = { reportId ->
                onNavigateTo(Screen.ReportScreen.navWith(reportId))
            }
        )
    }
}

fun NavGraphBuilder.newReportScreen(onNavigateTo: (Screen) -> Unit, viewModel: NavigationViewModel) {
    composable(route = Screen.NewReportScreen.route) {
        LaunchedEffect(key1 = viewModel.isBottomBarVisible.value) {
            viewModel.setBottomBarVisible(false)
        }
        val newReportViewModel = hiltViewModel<NewReportViewModel>()
        NewReportScreen(
            viewModel = newReportViewModel,
            navigateToHome = {
                onNavigateTo(Screen.ReportListScreen)
            }
        )
    }
}

fun NavGraphBuilder.reportScreen(onNavigateBack: () -> Unit, viewModel: NavigationViewModel) {
    composable(
        route = Screen.ReportScreen.withReportId(),
        arguments = listOf(
            navArgument(REPORT_ID) {
                type = NavType.StringType
            }
        )
    ) {
        LaunchedEffect(key1 = viewModel.isBottomBarVisible.value) {
            viewModel.setBottomBarVisible(false)
        }
        val reportViewModel = hiltViewModel<ReportViewModel>()
        ReportScreen(
            viewModel = reportViewModel,
            navigateBack = onNavigateBack
        )
    }
}

fun NavGraphBuilder.newsScreen() {
    composable(route = Screen.NewsScreen.route) {
        val viewModel = hiltViewModel<NewsViewModel>()
        NewsScreen(
            viewModel = viewModel
        )
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
            navigateToChangeUsername = { onNavigateTo(Screen.UsernameChangeScreen) },
            navigateToLogin = { onNavigateToLogin(Screen.LoginScreen) }
        )
    }
}

fun NavGraphBuilder.usernameChangeScreen(onNavigateBack: () -> Unit, viewModel: NavigationViewModel) {
    composable(route = Screen.UsernameChangeScreen.route) {
        LaunchedEffect(key1 = viewModel.isBottomBarVisible.value) {
            viewModel.setBottomBarVisible(false)
        }
        val profileViewModel = hiltViewModel<ProfileViewModel>()
        UsernameChangeScreen(
            viewModel = profileViewModel,
            navigateBack = onNavigateBack
        )
    }
}

fun NavGraphBuilder.settingsScreen(onNavigateBack: () -> Unit, viewModel: NavigationViewModel) {
    composable(route = Screen.SettingsScreen.route) {
        LaunchedEffect(key1 = viewModel.isBottomBarVisible.value) {
            viewModel.setBottomBarVisible(false)
        }
        val settingsViewModel = hiltViewModel<SettingsViewModel>()
        SettingsScreen(
            navigateBack = onNavigateBack,
            viewModel = settingsViewModel
        )
    }
}
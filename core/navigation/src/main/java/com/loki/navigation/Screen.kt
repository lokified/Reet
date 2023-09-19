package com.loki.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.ui.graphics.vector.ImageVector
import com.loki.ui.utils.Constants.CAMERA_SCREEN_TYPE
import com.loki.ui.utils.Constants.REPORT_ID
import com.loki.ui.utils.Constants.VIDEO_URI

sealed class Screen(
    val route: String,
    var routePath: String? = null,
    var clearBackStack: Boolean = false,
    val restoreState: Boolean = true,
    val title: String? = null,
    val icon: ImageVector? = null,
) {

    fun withClearBackStack() = apply { clearBackStack = true }

    fun navWith(path: String) = apply { routePath = path }

    fun withReportId(): String {
        return "${ReportScreen.route}/{$REPORT_ID}"
    }

    fun withCameraScreenType(): String {
        return  "${CameraScreen.route}/{$CAMERA_SCREEN_TYPE}"
    }

    fun withVideoUri(): String {
        return "${VideoPlayerScreen.route}/{$VIDEO_URI}"
    }

    object LoginScreen: Screen("login_screen")
    object RegisterScreen: Screen("register_screen")
    object ForgotPasswordScreen: Screen("forgot_password_screen")
    object HomeScreen: Screen("home_screen")
    object ReportListScreen: Screen(route = "report_list_screen", title = "Home", icon = Icons.Filled.Home)
    object NewsScreen: Screen(route = "news_screen", title = "News", restoreState = false, icon = Icons.Filled.Newspaper)
    object NewReportScreen: Screen("new_report_screen")
    object ReportScreen: Screen("report_screen", restoreState = false)
    object ProfileScreen: Screen("profile_screen", title = "Profile", icon = Icons.Filled.AccountCircle)
    object UsernameChangeScreen: Screen("change_username_screen")
    object SettingsScreen: Screen("settings_screen")
    object CameraScreen: Screen("camera_screen")
    object VideoPlayerScreen: Screen("video_player_screen")

}
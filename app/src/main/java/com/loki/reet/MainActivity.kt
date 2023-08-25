package com.loki.reet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.loki.reet.ui.MainApp
import com.loki.settings.SettingsViewModel
import com.loki.ui.theme.ReetTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setContent {

            val viewModel = hiltViewModel<SettingsViewModel>()
            val isDarkTheme by viewModel.isDarkTheme

            ReetTheme(
                darkTheme = isDarkTheme
            ) {
               MainApp()
            }
        }
    }
}
package com.loki.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    navigateToSettings: () -> Unit,
    navigateToLogin: () -> Unit
) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {


        Button(onClick = { viewModel.logOut(navigateToLogin) }) {
            Text(text = "Logout")
        }

    }

}
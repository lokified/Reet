package com.loki.profile.username

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.loki.profile.ProfileViewModel
import com.loki.ui.components.AppTopBar
import com.loki.ui.utils.TextFieldColorUtil

@Composable
fun UsernameChangeScreen(
    viewModel: ProfileViewModel,
    navigateBack: () -> Unit
) {

    val uiState by viewModel.state
    val focusRequester = remember { FocusRequester() }
    val float by animateFloatAsState(targetValue = 0f, label = "button_animation")

    Scaffold(
        topBar = {
            AppTopBar(
                leadingItem = {
                    IconButton(onClick = navigateBack) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                    }
                    Text(text = "Change Username", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }
            )
        }
    ) { padding ->

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                label = {
                    Text(
                        text = "Username",
                        color = MaterialTheme.colorScheme.onBackground.copy(.5f)
                    )
                },
                value = uiState.username,
                onValueChange = viewModel::onUsernameChange,
                placeholder = {
                    Text(
                        text = "Enter new username",
                        color = MaterialTheme.colorScheme.onBackground.copy(.5f)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                enabled = !viewModel.isLoading.value,
                colors = TextFieldColorUtil.colors(viewModel.isDarkTheme.value)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.updateUsername(
                        onSuccess = navigateBack
                    ) },
                enabled = !viewModel.isLoading.value,
                modifier = Modifier.fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(text = "Update Username")
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
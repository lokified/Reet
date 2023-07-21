package com.loki.auth.login

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.loki.ui.components.Loading
import com.loki.ui.components.NormalInput
import com.loki.ui.components.ProfileSetUpSheet

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    navigateToRegister: () -> Unit,
    navigateToHome: () -> Unit
) {

    val uiState by viewModel.state

    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycle = lifecycleOwner.lifecycle

    val textColor = if (viewModel.isLoading.value) MaterialTheme.colorScheme.onBackground.copy(.5f)
        else MaterialTheme.colorScheme.onBackground

    DisposableEffect(key1 = lifecycle) {
        val lifecycleObserver = LifecycleEventObserver { _, event ->
            when(event) {
                Lifecycle.Event.ON_PAUSE -> {
                    viewModel.errorMessage.value = ""
                }
                else -> {}
            }
        }

        lifecycle.addObserver(lifecycleObserver)

        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .align(Center),
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Lets Log you in",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )

            Spacer(modifier = Modifier.height(16.dp))

            NormalInput(
                placeholder = "Email Address",
                label = "Email",
                value = uiState.email,
                onValueChange = viewModel::onEmailChange,
                errorMessage = uiState.emailError,
                isError = uiState.isEmailError,
                leadingIcon = Icons.Filled.Email,
                isEnabled = !viewModel.isLoading.value
            )

            Spacer(modifier = Modifier.height(8.dp))

            NormalInput(
                placeholder = "Password",
                label = "Password",
                value = uiState.password,
                onValueChange = viewModel::onPasswordChange,
                errorMessage = uiState.passwordError,
                isError = uiState.isPasswordError,
                leadingIcon = Icons.Filled.Lock,
                keyboardType = KeyboardType.Password,
                isEnabled = !viewModel.isLoading.value
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Forgot Password?",
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .clickable { },
                color = textColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    keyboardController?.hide()
                    viewModel.login(navigateToHome)
                },
                enabled = !viewModel.isLoading.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(CenterHorizontally)
            ) {
                Text(text = "Login")
            }
        }

        Text(
            text = "Create an account with us",
            modifier = Modifier
                .padding(16.dp)
                .align(BottomCenter)
                .clickable {
                    if (!viewModel.isLoading.value) {
                        navigateToRegister()
                    }
                },
            color = textColor
        )
    }

    if (viewModel.isProfileSheetVisible.value) {
        ProfileSetUpSheet(
            onDismiss = {
                viewModel.isProfileSheetVisible.value = false
                viewModel.onCancelProfileSheet()
            },
            onComplete = {
                viewModel.setUpProfile(
                    navigateToHome = {
                        navigateToHome()
                        viewModel.isProfileSheetVisible.value = false
                    }
                )
            },
            isEnabled = !viewModel.isLoading.value,
            name = viewModel.completeProfileUserName.value
        ) {
            NormalInput(
                label = "Username",
                placeholder = "Username",
                value = uiState.userName,
                onValueChange = viewModel::onUsernameChange,
                errorMessage = uiState.userNameError,
                isError = uiState.isUserNameError,
                isEnabled = !viewModel.isLoading.value
            )
        }
    }

    if (viewModel.isLoading.value) {
        Loading()
    }

    if (viewModel.errorMessage.value.isNotBlank()) {
        LaunchedEffect(key1 = viewModel.errorMessage.value) {
            Toast.makeText(
                context,
                viewModel.errorMessage.value,
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
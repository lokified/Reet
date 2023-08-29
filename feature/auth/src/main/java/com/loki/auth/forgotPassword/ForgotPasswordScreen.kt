package com.loki.auth.forgotPassword

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.loki.ui.components.AppTopBar
import com.loki.ui.utils.TextFieldColorUtil.colors

@Composable
fun ForgotPasswordScreen(
    viewModel: ForgotPasswordViewModel,
    navigateBack: () -> Unit
) {

    val uiState by viewModel.state
    var isFieldVisible by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val float by animateFloatAsState(targetValue = 0f, label = "button_animation")

    Scaffold(
        topBar = {
            AppTopBar(
                leadingItem = {
                    IconButton(onClick = navigateBack) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                    }
                    Text(text = "Change Password", fontSize = 22.sp, fontWeight = FontWeight.Bold)
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

            if(isFieldVisible) {
                SendResetLinkContent(
                    value = uiState.email,
                    onValueChange = viewModel::onEmailChange,
                    error = uiState.emailError,
                    isError = uiState.isEmailError,
                    isEnabled = !viewModel.isLoading.value,
                    isDarkTheme = viewModel.isDarkTheme.value,
                    onSendClick = {
                        viewModel.sendResetLink {
                            isFieldVisible = false
                        }
                    }
                )
            }
            else{
                SuccessResetLinkContent {
                    navigateBack()
                }
            }

            Box(modifier = Modifier.fillMaxSize()) {
                if (viewModel.isLoading.value) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                if (viewModel.errorMessage.value.isNotBlank()) {
                    LaunchedEffect(key1 = viewModel.errorMessage.value ) {

                        Toast.makeText(
                            context,
                            viewModel.errorMessage.value,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }
}

@Composable
fun SendResetLinkContent(
    value: String,
    onValueChange: (String) -> Unit,
    error: String,
    isError: Boolean,
    isEnabled: Boolean,
    isDarkTheme: Boolean,
    onSendClick: () -> Unit
) {

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Text(text = "An Email will be sent with instructions on how to set up your new password.")

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            isError = isError,
            label = {
                Text(text = "Enter Email")
            },
            modifier = Modifier
                .fillMaxWidth(),
            supportingText = {
                if (isError) {
                    Text(text = error, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }
            },
            enabled = isEnabled,
            colors = colors(isDarkTheme)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onSendClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(text = "Send Password Reset Link")
        }
    }
}

@Composable
fun SuccessResetLinkContent(
    onLoginClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(24.dp)
    ) {

        Text(text = "Instructions has been sent to your email.")

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onLoginClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(text = "Go Back To Login")
        }
    }
}
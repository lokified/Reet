package com.loki.auth.register

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.loki.ui.components.Loading
import com.loki.ui.components.NormalInput
import com.loki.ui.components.ProfileSetUpSheet

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    navigateToLogin: () -> Unit
) {

    val uiState by viewModel.state

    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    var isSheetVisible by remember { mutableStateOf(false) }

    val textColor = if (viewModel.isLoading.value) MaterialTheme.colorScheme.onBackground.copy(.5f)
    else MaterialTheme.colorScheme.onBackground

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .align(Alignment.Center),
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Create an Account with Reet",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                NormalInput(
                    placeholder = "First Name",
                    label = "First Name",
                    value = uiState.firstName,
                    onValueChange = viewModel::onFirstNameChange,
                    errorMessage = uiState.firstNameError,
                    isError = uiState.isFirstNameError,
                    leadingIcon = Icons.Filled.AccountCircle,
                    modifier = Modifier.fillMaxWidth(.5f),
                    isEnabled = !viewModel.isLoading.value
                )

                NormalInput(
                    placeholder = "Last Name",
                    label = "Last Name",
                    value = uiState.lastName,
                    onValueChange = viewModel::onLastNameChange,
                    errorMessage = uiState.lastNameError,
                    isError = uiState.isLastNameError,
                    leadingIcon = Icons.Filled.AccountCircle,
                    isEnabled = !viewModel.isLoading.value
                )
            }
            

            Spacer(modifier = Modifier.height(8.dp))

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

            Spacer(modifier = Modifier.height(8.dp))

            NormalInput(
                placeholder = "Confirm Password",
                label = "Confirm Password",
                value = uiState.conPassword,
                onValueChange = viewModel::onConPasswordChange,
                errorMessage = uiState.conPasswordError,
                isError = uiState.isConPasswordError,
                leadingIcon = Icons.Filled.Lock,
                keyboardType = KeyboardType.Password,
                isEnabled = !viewModel.isLoading.value
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    keyboardController?.hide()
                    viewModel.register(
                        onRegister = {
                            isSheetVisible = true
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                enabled = !viewModel.isLoading.value
            ) {
                Text(text = "Sign Up")
            }

        }

        Text(
            text = "Already have an account, Login",
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomCenter)
                .clickable {
                    if (!viewModel.isLoading.value) {
                        navigateToLogin()
                    }
                },
            color = textColor
        )
    }

    if (isSheetVisible) {
        ProfileSetUpSheet(
            onDismiss = {
                viewModel.onCancelProfileSheet()
                isSheetVisible = false
            },
            onComplete = {
                viewModel.setUpProfile(
                    navigateToLogin = {
                        navigateToLogin()
                        isSheetVisible = false
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

    if(viewModel.isLoading.value) {
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
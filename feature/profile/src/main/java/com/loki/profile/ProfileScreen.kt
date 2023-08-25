package com.loki.profile

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.loki.ui.components.AppTopBar
import com.loki.ui.components.ExtendedRowItem
import com.loki.ui.components.ProfileCircleBox
import com.loki.ui.utils.ext.toInitials

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    navigateToSettings: () -> Unit,
    navigateToLogin: () -> Unit
) {

    val uiState by viewModel.state
    var openBottomSheet by rememberSaveable{ mutableStateOf(false) }

    val openIntent = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {}
    )

    Scaffold (
        topBar = {
            AppTopBar(
                leadingItem = {
                    Text(text = "Profile", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                },
                modifier = Modifier.padding(16.dp)
            )
        }
    ) { padding ->

        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                ProfileBox(
                    backgroundColor = Color(viewModel.localProfile.value.profileBackground),
                    initials = viewModel.localUser.value.name.toInitials(),
                    onEditClick = {}
                )
            }

            ExtendedRowItem(content = "Names", subContent = viewModel.localUser.value.name)
            ExtendedRowItem(content = "Email", subContent = viewModel.localUser.value.email)
            ExtendedRowItem(content = "Username", subContent = viewModel.localProfile.value.userName)

            Text(text = "SETTINGS", modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))

            RowItem(
                icon = Icons.Filled.Settings,
                iconBackground = Color(0xFFE33C75),
                content = "Change settings",
                onRowClick = navigateToSettings
            )
            
            Text(text = "SUPPORT", modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))

            RowItem(
                icon = Icons.Filled.Email,
                iconBackground = Color(0xFF95CF88),
                content = "Make request",
                onRowClick = {
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.type = "text/plain"
                    intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("sheldonokware@gmail.com"))
                    openIntent.launch(intent)
                }
            )

            RowItem(
                icon = Icons.Filled.Call,
                iconBackground = Color(0xFFA8719F),
                content = "Call Support",
                onRowClick = {
//                    val intent = Intent(Intent.ACTION_CALL)
//                    intent.data = Uri.parse("tel:254725992494")
//                    openIntent.launch(intent)
                }
            )

            RowItem(
                icon = Icons.Filled.Logout,
                iconBackground = Color(0xFFF1736A),
                content = "Logout",
                onRowClick = {
                    viewModel.logOut(navigateToLogin)
                }
            )

            Text(
                text = "1.0.0",
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
    if (openBottomSheet) {
        EditUsernameSheet(viewModel = viewModel, uiState = uiState) {
            openBottomSheet = false
        }
    }

    LaunchedEffect(key1 = Unit) {
        if (!viewModel.isLoading.value) {
            openBottomSheet = false
        }
    }
}

@Composable
fun ProfileBox(
    modifier: Modifier = Modifier,
    initials: String? = null,
    imageUrl: String? = null,
    backgroundColor: Color,
    onEditClick: () -> Unit
) {

    Box(
        modifier = modifier
            .size(100.dp)
            .background(
                color = backgroundColor,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        if (initials != null) {
            ProfileCircleBox(
                initials = initials,
                backgroundColor = backgroundColor,
                initialsSize = 30,
                modifier = Modifier.fillMaxSize()
            )
        }
        if (imageUrl != null) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "profile image",
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor),
                contentScale = ContentScale.Crop
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color.Black.copy(.5f),
                    shape = CircleShape
                )
                .clickable { onEditClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Change Profile",
                fontSize = 12.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun RowItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    iconBackground: Color,
    content: String,
    onRowClick: () -> Unit
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onRowClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 12.dp
            )
        ) {

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        shape = RoundedCornerShape(12.dp),
                        color = iconBackground
                    ),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = icon,
                    contentDescription = "row_icon",
                    tint = MaterialTheme.colorScheme.background
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = content)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditUsernameSheet(
    viewModel: ProfileViewModel,
    uiState: ProfileUiState,
    onDismiss: () -> Unit
) {

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        shape = RoundedCornerShape(
            topStart = 8.dp,
            topEnd = 8.dp
        )
    ) {

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
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
                        text = "Enter username",
                        color = MaterialTheme.colorScheme.onBackground.copy(.5f)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !viewModel.isLoading.value,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(.02f)
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = viewModel::updateUsername,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Update Username")
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
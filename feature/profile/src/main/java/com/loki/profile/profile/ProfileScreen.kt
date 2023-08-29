package com.loki.profile.profile

import android.content.Intent
import android.widget.Toast
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.loki.profile.ProfileViewModel
import com.loki.ui.components.AppTopBar
import com.loki.ui.components.ExtendedRowItem
import com.loki.ui.components.ProfileCircleBox

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    navigateToSettings: () -> Unit,
    navigateToChangeUsername: () -> Unit,
    navigateToLogin: () -> Unit
) {

    val localProfile by viewModel.localProfile.collectAsState()
    val localUser by viewModel.localUser.collectAsState()
    val context = LocalContext.current

    val openIntent = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {}
    )

    if (viewModel.errorMessage.value.isNotBlank()) {
        LaunchedEffect(key1 = viewModel.errorMessage.value) {
            Toast.makeText(
                context,
                viewModel.errorMessage.value,
                Toast.LENGTH_LONG
            ).show()
        }
    }

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
                    backgroundColor = Color(localProfile.profileBackground),
                    initials = localProfile.userNameInitials,
                    onEditClick = {}
                )
            }

            ExtendedRowItem(content = "Names", subContent = localUser.name)
            ExtendedRowItem(content = "Email", subContent = localUser.email)
            ExtendedRowItem(
                content = "Username",
                subContent = localProfile.userName,
                isEditable = true,
                onRowClick = {
                    navigateToChangeUsername()
                }
            )

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
                text = "v1.1.0",
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
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
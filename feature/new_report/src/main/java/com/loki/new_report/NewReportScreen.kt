package com.loki.new_report

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.sharp.Cancel
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.loki.ui.components.AppTopBar
import com.loki.ui.components.Loading
import com.loki.ui.components.ProfileCircleBox
import kotlinx.coroutines.job

@Composable
fun NewReportScreen(
    viewModel: NewReportViewModel,
    navigateToHome: () -> Unit,
    navigateToCamera: () -> Unit
) {

    val isDarkTheme by viewModel.isDarkTheme
    val uiState by viewModel.state
    val localProfile by viewModel.localProfile.collectAsStateWithLifecycle()

    val context = LocalContext.current

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(key1 = Unit) {
        this.coroutineContext.job.invokeOnCompletion {
            focusRequester.requestFocus()
        }
    }

    var isGalleryClicked by rememberSaveable { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        isGalleryClicked = false

        uri?.let {
            viewModel.onChangeImageUri(uri)
        }
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

    if (isGalleryClicked) {
        SideEffect {
            galleryLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
    }

    if (viewModel.isLoading.value) {
        Loading(
            alignment = Alignment.TopCenter
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {

        AppTopBar(
            modifier = Modifier.padding(horizontal = 16.dp),
            leadingItem = {
                Text(text = "New Report", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            },
            trailingItem = {
                Button(
                    onClick = {
                        viewModel.addReport(navigateToHome)
                    },
                    enabled = !viewModel.isLoading.value &&
                            (uiState.reportContent.isNotBlank() || uiState.imageUri != null)
                ) {
                    Text(text = "Add")
                }
            }
        )


        ProfileCircleBox(
            initials = localProfile.userNameInitials,
            backgroundColor = Color(localProfile.profileBackground),
            initialsSize = 20,
            modifier = Modifier
                .size(80.dp)
                .padding(16.dp),
            imageUri = localProfile.profileImage
        )

        Column {

            val containerColor = if (isDarkTheme) MaterialTheme.colorScheme.primary.copy(.2f)
            else MaterialTheme.colorScheme.primary.copy(.05f)

            TextField(
                value = uiState.reportContent,
                onValueChange = viewModel::onChangeReportContent,
                placeholder = {
                    Text(text = "What is Happening?", color = MaterialTheme.colorScheme.onBackground.copy(.5f))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp)
                    .height(150.dp)
                    .focusRequester(focusRequester),
                enabled = !viewModel.isLoading.value,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = containerColor,
                    unfocusedContainerColor = containerColor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = if (isDarkTheme) MaterialTheme.colorScheme.onBackground
                        else MaterialTheme.colorScheme.primary,
                    disabledContainerColor = Color.Transparent
                ),
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(16.dp)
            ) {
                IconButton(
                    onClick = navigateToCamera
                ) {
                    Icon(imageVector = Icons.Filled.CameraAlt, contentDescription = "Camera_icon")
                }
            }


            uiState.imageUri?.let {
                ImageContainer(
                    imageUri = it,
                    onDeleteClick = {
                        viewModel.onChangeImageUri(null)
                    }
                )
            }
        }
    }
}

@Composable
fun ImageContainer(
    imageUri: Uri,
    onDeleteClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 16.dp)
            .height(300.dp)
            .clip(RoundedCornerShape(8.dp))
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = imageUri),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(.5f))
        ) {
            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Sharp.Cancel,
                    contentDescription = "cancel icon"
                )
            }
        }
    }
}
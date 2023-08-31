package com.loki.new_report

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.loki.ui.components.ProfileCircleBox
import kotlinx.coroutines.job

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewReportScreen(
    viewModel: NewReportViewModel,
    navigateToHome: () -> Unit
) {

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
                    enabled = !viewModel.isLoading.value && uiState.reportContent.isNotBlank()
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
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = if (viewModel.isDarkTheme.value) MaterialTheme.colorScheme.primary.copy(.2f)
                        else MaterialTheme.colorScheme.primary.copy(.05f),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = if (viewModel.isDarkTheme.value) MaterialTheme.colorScheme.onBackground
                        else MaterialTheme.colorScheme.primary
                ),
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(16.dp)
            ) {

                IconButton(
                    onClick = {
                        isGalleryClicked = true
                    }
                ) {

                    Icon(
                        imageVector = Icons.Filled.Image,
                        contentDescription = "Gallery_icon"
                    )
                }
            }


            uiState.imageUri?.let {
                Image(
                    painter = rememberAsyncImagePainter(model = it),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(vertical = 4.dp, horizontal = 16.dp)
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }

    }
}
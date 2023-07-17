package com.loki.new_report

import android.Manifest
import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.provider.MediaStore
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.loki.ui.components.AppTopBar
import com.loki.ui.components.ProfileCircleBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewReportScreen(
    viewModel: NewReportViewModel,
    navigateToHome: () -> Unit
) {

    val uiState by viewModel.state

    val context = LocalContext.current

    val activity = remember { context as? Activity }

//    val focusRequester = remember { FocusRequester() }
//
//    LaunchedEffect(key1 = Unit) {
//        focusRequester.requestFocus()
//    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->

        uri?.let {
            viewModel.onChangeImageUri(uri)
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
            initials = viewModel.userInitial.value,
            backgroundColor = Color(viewModel.profileBackground.value),
            initialsSize = 20,
            modifier = Modifier
                .size(80.dp)
                .padding(16.dp)
        )

        Column {

            uiState.imageUri?.let {
                Image(
                    bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, it).asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp)
                        .size(150.dp),
                    contentScale = ContentScale.Fit
                )
            }

            TextField(
                value = uiState.reportContent,
                onValueChange = viewModel::onChangeReportContent,
                placeholder = {
                    Text(text = "What is Happening?", color = MaterialTheme.colorScheme.onBackground.copy(.5f))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp)
                    .height(150.dp),
                enabled = !viewModel.isLoading.value,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(.02f),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(16.dp)
            ) {

                IconButton(
                    onClick = {
                        galleryLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                ) {

                    Icon(
                        imageVector = Icons.Filled.Image,
                        contentDescription = "Gallery_icon"
                    )
                }
            }
        }

    }

    if (viewModel.errorMessage.value.isNotBlank()) {
        LaunchedEffect(key1 = Unit) {
            Toast.makeText(
                context,
                viewModel.errorMessage.value,
                Toast.LENGTH_LONG
            ).show()
        }
    }
}

//private fun checkPermission(activity: Activity): Boolean {
//
//    return if (activity.checkSelfPermission( WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//        true
//    }
//    else if(activity.checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED) {
//        true
//    }
//    else {
//        ActivityCompat.requestPermissions(
//            activity, arrayOf(
//                READ_EXTERNAL_STORAGE,
//                WRITE_EXTERNAL_STORAGE,
//                CAMERA
//            ), 1
//        ); false
//    }
//}
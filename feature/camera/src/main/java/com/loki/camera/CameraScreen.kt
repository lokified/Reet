package com.loki.camera

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import com.loki.camera.util.StatusBarUtil
import com.loki.profile.ProfileViewModel
import com.loki.ui.components.Loading
import com.loki.ui.theme.md_theme_dark_background
import com.loki.ui.theme.md_theme_dark_secondaryContainer

private val EMPTY_IMAGE_URI: Uri = Uri.parse("file://dev/null")

@Composable
fun CameraScreen(
    profileViewModel: ProfileViewModel,
    navigateBack: () -> Unit
) {

    val appTheme by profileViewModel.isDarkTheme

    val context = LocalContext.current
    val view = LocalView.current

    var imageUri by remember { mutableStateOf(EMPTY_IMAGE_URI) }
    var isGalleryClicked by rememberSaveable { mutableStateOf(false) }

    // if not dark theme change colors to dark theme in this screen composable
    var statusBarColor by remember { mutableStateOf(md_theme_dark_background.toArgb()) }
    var isDark by remember { mutableStateOf(appTheme) }
    var navBarColor by remember { mutableStateOf(md_theme_dark_background.toArgb()) }

    val defaultNavColor = StatusBarUtil.defaultNavColor(darkTheme = appTheme)
    val defaultStatusBarColor = StatusBarUtil.statusBarColor()

    if (!appTheme) {
        StatusBarUtil.DefaultStatusColors(
            view = view,
            isDark = isDark,
            statusBarColor = statusBarColor,
            navigationBarColor = navBarColor
        )

        BackHandler(true) {
            statusBarColor = defaultStatusBarColor
            navBarColor = defaultNavColor
            isDark = !isDark
            navigateBack()
        }
    }

    // open files to select images
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        isGalleryClicked = false

        uri?.let {
            imageUri = it
        }
    }

    if (isGalleryClicked) {
        SideEffect {
            galleryLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
    }

    // onBackPress will not navigate back if image has been captured or selected
    if (imageUri != EMPTY_IMAGE_URI) {
        BackHandler(true) {
            imageUri = EMPTY_IMAGE_URI
        }
    }

    if (profileViewModel.errorMessage.value.isNotBlank()) {
        LaunchedEffect(key1 = profileViewModel.errorMessage.value) {
            Toast.makeText(
                context,
                profileViewModel.errorMessage.value,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    if (imageUri != EMPTY_IMAGE_URI) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(md_theme_dark_background)
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = rememberAsyncImagePainter(imageUri),
                contentDescription = "Captured image"
            )
            IconButton(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.onBackground,
                    containerColor = md_theme_dark_secondaryContainer
                ),
                onClick = {
                    imageUri = EMPTY_IMAGE_URI
                }
            ) {
                Image(
                    imageVector = Icons.Filled.Sync,
                    contentDescription = "Retry icon",
                    modifier = Modifier.size(30.dp),
                    colorFilter = ColorFilter.tint(
                        Color.White
                    )
                )
            }
            IconButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.onBackground,
                    containerColor =  md_theme_dark_secondaryContainer
                ),
                onClick = {
                    profileViewModel.updateProfilePicture(imageUri) {
                        navigateBack()
                    }
                }
            ) {
                Image(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Done icon",
                    modifier = Modifier.size(30.dp),
                    colorFilter = ColorFilter.tint(
                        Color.White
                    )
                )
            }

            if (profileViewModel.isLoading.value) {
                Loading()
            }
        }
    } else {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(md_theme_dark_background)
        ) {
            CameraCapture(
                modifier = Modifier.fillMaxSize(),
                onImageFile = { file ->
                    imageUri = file.toUri()
                },
                onGalleryClick = {
                    isGalleryClicked = true
                }
            )
        }
    }
}
package com.loki.camera.camera_screen

import android.Manifest
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.loki.camera.util.StatusBarUtil
import com.loki.profile.ProfileViewModel
import com.loki.ui.components.Loading
import com.loki.ui.permission.PermissionAction
import com.loki.ui.permission.PermissionDialog
import com.loki.ui.theme.md_theme_dark_background
import com.loki.ui.theme.md_theme_dark_secondaryContainer

private val EMPTY_IMAGE_URI: Uri = Uri.parse("file://dev/null")

@Composable
fun CameraScreen(
    profileViewModel: ProfileViewModel,
    navigateToVideoPlayer: (uri: String) -> Unit,
    navigateBack: () -> Unit
) {

    val appTheme by profileViewModel.isDarkTheme

    val context = LocalContext.current
    val view = LocalView.current

    val snackbarHostState = remember { SnackbarHostState() }
    var showPermissionDialog by remember { mutableStateOf(false) }
    var isPermissionGranted by remember { mutableStateOf(false) }

    // camera states
    var isCameraView by remember { mutableStateOf(true) }
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

    val permissions = listOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
    val permissionRationales = mapOf(
        Manifest.permission.CAMERA to "Camera permission is required for taking photos and video recording",
        Manifest.permission.RECORD_AUDIO to "Record Audio permission is required for video recording"
    )

    if (showPermissionDialog) {
        PermissionDialog(
            context = context,
            permissions = permissions,
            permissionRationale = permissionRationales,
            snackbarHostState = snackbarHostState,
            permissionAction = { permissionsAction ->

                for ((permission, action) in permissionsAction) {
                    when (action) {
                        is PermissionAction.PermissionGranted -> {
                            isPermissionGranted = true
                            Toast.makeText(
                                context,
                                "Permission Granted",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        is PermissionAction.PermissionDenied -> {
                            isPermissionGranted = false
                            Toast.makeText(
                                context,
                                "Permission Not Granted",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        is PermissionAction.PermissionAlreadyGranted -> {
                            isPermissionGranted = true
                        }
                    }
                }
            }
        )
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
                    containerColor = md_theme_dark_secondaryContainer
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

        if (isCameraView) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(md_theme_dark_background)
            ) {
                CameraCapture(
                    modifier = Modifier.fillMaxSize(),
                    isCameraView = isCameraView,
                    isPermissionGranted = isPermissionGranted,
                    onImageUri = { uri ->
                        imageUri = uri
                    },
                    onPermissionRequired = {
                        showPermissionDialog = it
                    },
                    onGalleryClick = {
                        isGalleryClicked = true
                    }
                ) {
                    BottomContent(
                        onViewSelected = {
                            isCameraView = it == Preview.CAMERA
                        }
                    )
                }
            }

        } else {

            VideoCapture(
                modifier = Modifier.fillMaxSize(),
                isCameraView = isCameraView,
                isPermissionGranted = isPermissionGranted,
                onVideoUri = { uri ->
                    uri?.let(navigateToVideoPlayer)
                },
                onPermissionRequired = {
                      showPermissionDialog = it
                },
                bottomSwitchContent = {
                    BottomContent(
                        onViewSelected = {
                            isCameraView = it == Preview.CAMERA
                        }
                    )
                }
            )
        }
    }
}

@Composable
fun BottomContent(
    modifier: Modifier = Modifier,
    onViewSelected: (Preview) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {

        val titles = listOf("Camera", "Video")
        var selectedTitle by remember { mutableStateOf(titles[0]) }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            titles.forEachIndexed { index, title ->

                val selected = titles[index] == title

                PreviewSelectables(
                    title = title,
                    selected = selected,
                    onClick = {
                        selectedTitle = title
                        if (title == "Camera") {
                            onViewSelected(Preview.CAMERA)
                        } else {
                            onViewSelected(Preview.VIDEO)
                        }
                    }
                )
            }
        }
    }
}

enum class Preview {
    CAMERA,
    VIDEO
}

@Composable
fun PreviewSelectables(
    modifier: Modifier = Modifier,
    title: String,
    titleSize: Int = 18,
    cornerRadius: Dp = 16.dp,
    spacing: Dp = 8.dp,
    selected: Boolean,
    selectedBackground: Color = Color.Black.copy(.3f),
    selectedTitleColor: Color = Color.White,
    onClick: () -> Unit
) {

    val backgroundColor by rememberUpdatedState( if (selected) selectedBackground else Color.Transparent)
    val titleColor by rememberUpdatedState( if(selected) selectedTitleColor else Color.White.copy(.5f))

    Box(
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(cornerRadius)
            )
            .clip(RoundedCornerShape(cornerRadius))
            .clickable { onClick() }
    ) {
        Text(
            text = title,
            color = titleColor,
            fontSize = titleSize.sp,
            modifier = Modifier.padding(spacing)
        )
    }
}
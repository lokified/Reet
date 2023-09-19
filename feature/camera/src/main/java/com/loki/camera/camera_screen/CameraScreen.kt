package com.loki.camera.camera_screen

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
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
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import coil.compose.rememberAsyncImagePainter
import com.loki.camera.R
import com.loki.camera.util.StatusBarUtil
import com.loki.profile.ProfileViewModel
import com.loki.ui.components.Loading
import com.loki.ui.permission.PermissionAction
import com.loki.ui.permission.PermissionDialog
import com.loki.ui.permission.checkIfPermissionGranted
import com.loki.ui.theme.md_theme_dark_background
import com.loki.ui.theme.md_theme_dark_secondaryContainer
import com.loki.ui.utils.Constants.SCREEN_TYPE_BOTH
import com.loki.ui.utils.Constants.SCREEN_TYPE_PROFILE_CAMERA
import com.loki.ui.utils.Constants.SCREEN_TYPE_REPORT_CAMERA

private val EMPTY_IMAGE_URI: Uri = Uri.parse("file://dev/null")

@Composable
fun CameraScreen(
    profileViewModel: ProfileViewModel,
    screenType: String,
    navigateToVideoPlayer: (uri: String) -> Unit,
    onSaveImage: (imageUri: Uri, screenSource: String) -> Unit,
    navigateBack: () -> Unit
) {

    val isDarkTheme by profileViewModel.isDarkTheme

    val context = LocalContext.current
    val view = LocalView.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycle = lifecycleOwner.lifecycle

    val snackbarHostState = remember { SnackbarHostState() }
    var showPermissionDialog by remember { mutableStateOf(false) }
    var isPermissionGranted by remember { mutableStateOf(false) }

    // preview
    var screenPreview by remember { mutableStateOf(ScreenPreview.CAMERA) }

    // camera states
    var imageUri by remember { mutableStateOf(EMPTY_IMAGE_URI) }
    var isGalleryClicked by rememberSaveable { mutableStateOf(false) }

    // if not dark theme change colors to dark theme in this screen composable
    var statusBarColor by remember { mutableStateOf(md_theme_dark_background.toArgb()) }
    var isDark by remember { mutableStateOf(isDarkTheme) }
    var navBarColor by remember { mutableStateOf(md_theme_dark_background.toArgb()) }

    val defaultNavColor = StatusBarUtil.defaultNavColor(darkTheme = isDarkTheme)
    val defaultStatusBarColor = StatusBarUtil.defaultStatusBarColor()

    if (!isDarkTheme) {
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

    // permissions for camera and audio recorder
    val permissions = listOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
    val permissionRationales = mapOf(
        Manifest.permission.CAMERA to "Camera permission is required for taking photos and video recording",
        Manifest.permission.RECORD_AUDIO to "Record Audio permission is required for video recording"
    )
    val permissionToRequest = if (screenType == SCREEN_TYPE_PROFILE_CAMERA || screenType == SCREEN_TYPE_REPORT_CAMERA)
        listOf(Manifest.permission.CAMERA) else permissions

    LaunchedEffect(key1 = showPermissionDialog, key2 = isPermissionGranted) {
        showPermissionDialog = true

        val isGranted = checkIfPermissionGranted(context, Manifest.permission.CAMERA)
        isPermissionGranted = isGranted
    }

    // onresume, check if permission was allowed
    DisposableEffect(key1 = lifecycle) {
        val lifecycleObserver = LifecycleEventObserver { _, event ->
            when(event) {
                Lifecycle.Event.ON_RESUME -> {
                    val isGranted = checkIfPermissionGranted(context, Manifest.permission.CAMERA)
                    isPermissionGranted = isGranted
                }
                else -> {}
            }
        }

        lifecycle.addObserver(lifecycleObserver)

        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }

    if (showPermissionDialog) {
        PermissionDialog(
            context = context,
            permissions = permissions,
            permissionToRequest = permissionToRequest,
            permissionRationale = permissionRationales,
            snackbarHostState = snackbarHostState,
            permissionAction = { permissionsAction ->

                for ((permission, action) in permissionsAction) {
                    when (action) {
                        is PermissionAction.PermissionGranted -> {
                            isPermissionGranted = true
                        }

                        is PermissionAction.PermissionDenied -> {
                            isPermissionGranted = false
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
                    statusBarColor = defaultStatusBarColor
                    navBarColor = defaultNavColor
                    isDark = !isDark
                    onSaveImage(imageUri, screenType)
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

        if (!isPermissionGranted) {

            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier.align(Alignment.Center)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.allow_permissions),
                        textAlign = TextAlign.Center
                    )
                    OutlinedButton(
                        onClick = {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri = Uri.fromParts("package", context.packageName, null)
                            intent.data = uri
                            context.startActivity(intent)
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.open_settings),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
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
                    screenPreview = screenPreview,
                    onImageUri = { uri ->
                        imageUri = uri
                    },
                    onVideoOutput = { output ->
                        output.uri?.let(navigateToVideoPlayer)
                    },
                    onGalleryClick = {
                        isGalleryClicked = true
                    },
                    onErrorMessage = {
                        Toast.makeText(
                            context,
                            it,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                ) {

                    if (screenType == SCREEN_TYPE_BOTH) {
                        BottomContent(
                            onViewSelected = {
                                screenPreview = it
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BottomContent(
    modifier: Modifier = Modifier,
    onViewSelected: (ScreenPreview) -> Unit
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

            titles.forEachIndexed { _, title ->

                PreviewSelectables(
                    title = title,
                    selected = selectedTitle == title,
                    onClick = {
                        selectedTitle = title
                        if (title == "Camera") {
                            onViewSelected(ScreenPreview.CAMERA)
                        } else {
                            onViewSelected(ScreenPreview.VIDEO)
                        }
                    }
                )
            }
        }
    }
}

enum class ScreenPreview {
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
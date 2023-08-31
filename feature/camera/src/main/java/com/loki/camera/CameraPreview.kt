package com.loki.camera

import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY
import androidx.camera.core.Preview
import androidx.camera.core.UseCase
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.loki.camera.util.executor
import com.loki.camera.util.getCameraProvider
import com.loki.camera.util.takePicture
import com.loki.ui.theme.md_theme_dark_background
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    scaleType: PreviewView.ScaleType = PreviewView.ScaleType.FILL_CENTER,
    onUseCase: (UseCase) -> Unit = {}
) {
    
    AndroidView(
        modifier = modifier,
        factory = { context ->
            val previewView = PreviewView(context).apply {
                this.scaleType = scaleType
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }

            // camerax preview usecase
            onUseCase(
                Preview.Builder()
                    .build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }
            )

            previewView
        }
    )
}

@Composable
fun CameraCapture(
    modifier: Modifier = Modifier,
    onImageFile: (File) -> Unit,
    onGalleryClick: () -> Unit
) {

    Box(modifier = modifier) {

        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current
        val coroutineScope = rememberCoroutineScope()

        var previewUseCase by remember { mutableStateOf<UseCase>(Preview.Builder().build()) }
        val imageCaptureUseCase by remember {
            mutableStateOf(
                ImageCapture.Builder().setCaptureMode(CAPTURE_MODE_MAXIMIZE_QUALITY)
                    .build()
            )
        }

        var isFrontCamera by remember { mutableStateOf(false) }

        val cameraSelector = if (isFrontCamera) CameraSelector.DEFAULT_FRONT_CAMERA
        else CameraSelector.DEFAULT_BACK_CAMERA

        Box(
            modifier = Modifier.background(md_theme_dark_background)
        ) {
            CameraPreview(
                modifier = Modifier.fillMaxSize(),
                onUseCase = {
                    previewUseCase = it
                }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
                    .align(Alignment.BottomCenter)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    IconButton(
                        onClick = onGalleryClick,
                        modifier = Modifier
                            .padding(start = 24.dp),
                    ) {
                        Image(
                            imageVector = Icons.Filled.Image,
                            contentDescription = "image_icon",
                            modifier = Modifier.size(30.dp),
                            colorFilter = ColorFilter.tint(
                                Color.White
                            )
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(.8f))
                            .clickable {
                                coroutineScope.launch(Dispatchers.IO) {
                                    imageCaptureUseCase
                                        .takePicture(context.executor)
                                        .let {
                                            onImageFile(it)
                                        }
                                }
                            }
                    )
                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(
                        onClick = {
                            isFrontCamera = !isFrontCamera
                        },
                        modifier = Modifier
                            .padding(end = 24.dp)
                    ) {
                        Image(
                            imageVector = Icons.Filled.Cameraswitch,
                            contentDescription = "rotate_camera_icon",
                            modifier = Modifier.size(30.dp),
                            colorFilter = ColorFilter.tint(
                                Color.White
                            )
                        )
                    }
                }
            }
        }

        LaunchedEffect(key1 = cameraSelector) {
            val cameraProvider = context.getCameraProvider()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    previewUseCase,
                    imageCaptureUseCase
                )
            } catch (e: Exception) {
                Log.e("Camera Capture", "Failed to bind camera use case", e)
            }
        }
    }
}
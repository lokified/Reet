package com.loki.camera.camera_screen

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY
import androidx.camera.core.Preview
import androidx.camera.core.UseCase
import androidx.camera.video.FallbackStrategy
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.camera.video.VideoRecordEvent.Finalize
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.core.util.Consumer
import androidx.lifecycle.LifecycleOwner
import com.loki.camera.util.executor
import com.loki.camera.util.getCameraProvider
import com.loki.camera.util.takePicture
import com.loki.ui.theme.md_theme_dark_background
import com.loki.ui.utils.DateUtil
import kotlinx.coroutines.launch
import java.io.File
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

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
    screenPreview: ScreenPreview,
    onImageUri: (imageUri: Uri) -> Unit,
    onVideoOutput: (VideoOutput) -> Unit,
    onErrorMessage: (String) -> Unit,
    onGalleryClick: () -> Unit,
    bottomSwitchContent: @Composable () -> Unit
) {

    Box(modifier = modifier) {

        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current
        val coroutineScope = rememberCoroutineScope()

        var previewUseCase by remember { mutableStateOf<UseCase>(Preview.Builder().build()) }

        // camera capture
        val imageCaptureUseCase by remember {
            mutableStateOf(
                ImageCapture.Builder().setCaptureMode(CAPTURE_MODE_MAXIMIZE_QUALITY)
                    .build()
            )
        }

        // video capture
        val qualitySelector = QualitySelector.fromOrderedList(
            listOf(Quality.UHD, Quality.FHD, Quality.HD, Quality.SD),
            FallbackStrategy.lowerQualityOrHigherThan(Quality.SD)
        )
        val record = Recorder.Builder()
            .setExecutor(context.executor)
            .setQualitySelector(qualitySelector)
            .setTargetVideoEncodingBitRate(5 * 1024 * 1024)
            .build()
        val videoCaptureUseCase = VideoCapture.withOutput(record)

        // video controls
        var recording = remember<Recording?> { null }
        var recordingStarted by remember { mutableStateOf(false) }
        var audioEnabled by remember { mutableStateOf(true) }

        // camera controls
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

            when(screenPreview) {
                ScreenPreview.CAMERA -> {
                    ImageCapture(
                        context = context,
                        lifecycleOwner = lifecycleOwner,
                        previewUseCase = previewUseCase,
                        cameraSelector = cameraSelector,
                        imageCaptureUseCase = imageCaptureUseCase
                    )
                }

                ScreenPreview.VIDEO -> {
                    VideoRecordCapture(
                        context = context,
                        lifecycleOwner = lifecycleOwner,
                        previewUseCase = previewUseCase,
                        cameraSelector = cameraSelector,
                        videoCaptureUseCase = videoCaptureUseCase
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {

                CaptureControls(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    screenPreview = screenPreview,
                    isRecording = recordingStarted,
                    onLeftControlClick = onGalleryClick,
                    onRotateCamera = {
                        isFrontCamera = !isFrontCamera
                    },
                    onCapture = {
                        when(screenPreview) {
                            ScreenPreview.CAMERA -> {
                                coroutineScope.launch {
                                    imageCapture(
                                        imageCaptureUseCase,
                                        context,
                                        onImageUri
                                    )
                                }
                            }

                            ScreenPreview.VIDEO -> {
                                if (!recordingStarted) {
                                    recordingStarted = true
                                    recording = startRecordingVideo(
                                        context = context,
                                        videoCapture = videoCaptureUseCase,
                                        audioEnabled = audioEnabled,
                                        consumer = { event ->
                                            onVideoRecorded(
                                                recording = recording,
                                                event = event,
                                                onVideoUri = onVideoOutput,
                                                onErrorMessage = onErrorMessage
                                            )
                                        }
                                    )
                                }
                                else {
                                    recordingStarted = false
                                    recording?.stop()
                                    recording = null
                                }
                            }
                        }
                    }
                )

                bottomSwitchContent()
            }
        }
    }
}

@Composable
fun ImageCapture(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    previewUseCase: UseCase,
    cameraSelector: CameraSelector,
    imageCaptureUseCase: ImageCapture
) {

    LaunchedEffect(key1 = cameraSelector, key2 = previewUseCase) {

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

@Composable
fun VideoRecordCapture(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    previewUseCase: UseCase,
    cameraSelector: CameraSelector,
    videoCaptureUseCase: VideoCapture<Recorder>,
) {

    LaunchedEffect(key1 = cameraSelector, key2 = previewUseCase) {

        val cameraProvider = context.getCameraProvider()

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                previewUseCase,
                videoCaptureUseCase
            )
        } catch (e: Exception) {
            Log.e("Camera Capture", "Failed to bind camera use case", e)
        }
    }
}

fun onVideoRecorded(
    recording: Recording?,
    event: VideoRecordEvent,
    onVideoUri: (VideoOutput) -> Unit,
    onErrorMessage: (String) -> Unit
) {

    when (event) {

        is Finalize -> {
            recording?.stop()

            val uri = event.outputResults.outputUri

            when(event.error) {
                Finalize.ERROR_INSUFFICIENT_STORAGE -> {
                    onErrorMessage("insufficient storage")
                }
                Finalize.ERROR_NO_VALID_DATA -> {
                    onErrorMessage("No data recorded")
                }
            }

            if (uri != Uri.EMPTY) {

                recording?.close()

                val uriEncoded = URLEncoder.encode(
                    uri.toString(),
                    StandardCharsets.UTF_8.toString()
                )
                onVideoUri(
                    VideoOutput(
                        uri = uriEncoded.toString(),
                    )
                )
            }
        }
    }
}

suspend fun imageCapture(
    imageCaptureUseCase: ImageCapture,
    context: Context,
    onImageUri: (Uri) -> Unit
) {
    imageCaptureUseCase
        .takePicture(context.executor)
        .let {
            onImageUri(it.toUri())
        }
}

@SuppressLint("MissingPermission")
fun startRecordingVideo(
    context: Context,
    videoCapture: VideoCapture<Recorder>,
    audioEnabled: Boolean,
    consumer: Consumer<VideoRecordEvent>
): Recording {

    val mediaDir = context.externalCacheDirs.firstOrNull()?.let {
        File(it, "Reet").apply { mkdirs() }
    }

    val outputDirectory =
        if (mediaDir != null && mediaDir.exists()) mediaDir else
            context.filesDir

    val videoFile = File(
        outputDirectory,
        DateUtil.getFileName() + ".mp4"
    )

    val outputOptions = FileOutputOptions.Builder(videoFile).build()

    return videoCapture.output
        .prepareRecording(context, outputOptions)
        .apply {
            if (audioEnabled) withAudioEnabled()
        }
        .start(context.executor, consumer)

}

@Composable
fun CaptureControls(
    modifier: Modifier = Modifier,
    screenPreview: ScreenPreview,
    isRecording: Boolean = false,
    audioEnabled: Boolean = true,
    onLeftControlClick: () -> Unit,
    onRotateCamera: () -> Unit,
    onCapture: () -> Unit
) {

    val recordingBackground = if (isRecording) Color.Red else Color.Red.copy(.4f)
    val captureBackground = if (screenPreview == ScreenPreview.CAMERA) Color.White.copy(.8f) else recordingBackground

    val recordingLeftControlIcon = if (audioEnabled) Icons.Filled.Mic else Icons.Filled.MicOff
    val leftControl = if (screenPreview == ScreenPreview.CAMERA) Icons.Filled.Image else recordingLeftControlIcon

    Box(
        modifier = modifier
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            IconButton(
                onClick = onLeftControlClick,
                modifier = Modifier
                    .padding(start = 24.dp),
            ) {
                Icon(
                    imageVector = leftControl,
                    contentDescription = "image_icon",
                    modifier = Modifier.size(30.dp),
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                    .background(captureBackground)
                    .border(
                        width = if (isRecording) 2.dp else 0.dp,
                        color = if (isRecording) Color.White else Color.Transparent,
                        shape = CircleShape
                    )
                    .clickable {
                        onCapture()
                    }
            )
            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = onRotateCamera,
                modifier = Modifier
                    .padding(end = 24.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Cameraswitch,
                    contentDescription = "rotate_camera_icon",
                    modifier = Modifier.size(30.dp),
                    tint = Color.White
                )
            }
        }
    }
}
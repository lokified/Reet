package com.loki.camera.camera_screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.loki.ui.utils.Constants.CAMERA_SCREEN_TYPE
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private var screenType = mutableStateOf("")
    init {
        savedStateHandle.get<String>(CAMERA_SCREEN_TYPE)?.let { type ->
            screenType.value = type
        }
    }
}
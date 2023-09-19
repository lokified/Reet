package com.loki.camera.video_screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.loki.ui.utils.Constants.VIDEO_URI
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VideoPlayerViewModel @Inject constructor(
    val player: Player,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    init {
        savedStateHandle.get<String>(VIDEO_URI)?.let {
            playVideo(it)
        }
        player.prepare()
    }

    private fun playVideo(uri: String) {
        player.setMediaItem(
            MediaItem.fromUri(uri)
        )
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }
}
package com.loki.navigation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class NavigationViewModel: ViewModel() {

    private val _isBottomBarVisible = mutableStateOf(true)
    val isBottomBarVisible : State<Boolean> = _isBottomBarVisible

    fun setBottomBarVisible(visible: Boolean) {
        _isBottomBarVisible.value = visible
    }
}
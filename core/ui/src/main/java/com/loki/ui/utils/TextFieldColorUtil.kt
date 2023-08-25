package com.loki.ui.utils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object TextFieldColorUtil {

    @Composable
    fun defaultColorField() =  if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onBackground
        else MaterialTheme.colorScheme.primary

    @Composable
    fun colors() = TextFieldDefaults.colors (
        focusedIndicatorColor = defaultColorField(),
        cursorColor = defaultColorField(),
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        focusedLabelColor =  defaultColorField(),
    )
}
package com.loki.ui.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object TextFieldColorUtil {

    @Composable
    private fun defaultColorField(isDarkTheme: Boolean) =  if (isDarkTheme) MaterialTheme.colorScheme.onBackground
        else MaterialTheme.colorScheme.primary

    @Composable
    fun colors(isDarkTheme: Boolean) = TextFieldDefaults.colors (
        focusedIndicatorColor = defaultColorField(isDarkTheme),
        cursorColor = defaultColorField(isDarkTheme),
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        focusedLabelColor =  defaultColorField(isDarkTheme),
    )
}
package com.loki.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun Loading(
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center
) {
    Box(
        modifier = modifier.fillMaxSize()
            .background(
                MaterialTheme.colorScheme.background.copy(.5f)
            ),
        contentAlignment = alignment
    ) {
        CircularProgressIndicator()
    }
}
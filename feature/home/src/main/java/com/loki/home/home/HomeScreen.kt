package com.loki.home.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    nestedNavGraph: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit
) {

    val color = MaterialTheme.colorScheme.background

    Scaffold(bottomBar = bottomBar) { padding ->

        Surface(
            modifier = Modifier.fillMaxSize().padding(padding),
            color = if (color == Color.Unspecified) Color.Transparent else color
        ) {
            CompositionLocalProvider(
                LocalAbsoluteTonalElevation provides 0.dp
            ) {
                nestedNavGraph.invoke()
            }
        }
    }
}
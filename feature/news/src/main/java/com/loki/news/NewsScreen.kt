package com.loki.news

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun NewsScreen() {

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

        Text(text = "News Screen")
    }

}
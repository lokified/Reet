package com.loki.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.loki.ui.theme.md_theme_light_primary
import kotlinx.coroutines.delay

@Composable
fun NotificationBubble(
    modifier: Modifier = Modifier,
    message: String = ""
) {

    var isBubbleVisible by remember { mutableStateOf(false) }
    var mess by remember { mutableStateOf(message) }

    LaunchedEffect(key1 = mess) {

        isBubbleVisible = true

        delay(3000L)

        isBubbleVisible = false
        mess = ""
    }

    AnimatedVisibility(
        visible = isBubbleVisible,
        enter = slideInVertically { -(it / 2) },
        exit = slideOutVertically { 0 }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 24.dp,
                        vertical = 16.dp
                    )
                    .clip(RoundedCornerShape(12.dp))
                    .height(70.dp)
                    .background(md_theme_light_primary.copy(.5f))
                    .border(
                        border = BorderStroke(width = 1.dp, color = md_theme_light_primary),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .alpha(1f)
                    .align(Alignment.TopCenter),
                contentAlignment = Alignment.CenterStart
            ) {

                Text(
                    text = message,
                    color = md_theme_light_primary,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

            }
        }
    }
}
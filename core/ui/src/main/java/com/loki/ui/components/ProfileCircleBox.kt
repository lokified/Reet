package com.loki.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import java.util.Locale

@Composable
fun ProfileCircleBox(
    modifier: Modifier = Modifier,
    initials: String,
    imageUri: String,
    backgroundColor: Color,
    initialsSize: Int,
    size: Dp = 40.dp
) {

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape),
        contentAlignment = Alignment.Center
    ) {

        if (imageUri.isNotBlank()) {
            AsyncImage(
                model = imageUri,
                contentDescription = "profile image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
                    .clip(CircleShape)
            )
        }
        else {
            Box(
                modifier = modifier
                    .size(size)
                    .clip(CircleShape)
                    .background(color = backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initials.uppercase(Locale.ENGLISH),
                    fontSize = initialsSize.sp,
                    color = Color.White
                )
            }
        }
    }
}
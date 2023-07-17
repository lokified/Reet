package com.loki.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileSetUpSheet(
    onDismiss: () -> Unit,
    onComplete: () -> Unit,
    isEnabled: Boolean = true,
    name: String,
    input: @Composable () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {

        IconButton(
            onClick = onDismiss,
            modifier = Modifier
                .padding(12.dp)
                .align(Alignment.TopEnd),
            enabled = isEnabled
        ) {
            Icon(
                imageVector = Icons.Filled.Cancel,
                contentDescription = null
            )
        }


        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp)
        ) {

            Text(
                text = "$name set up your username",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground.copy(.5f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            input()
        }

        Box(
            modifier = Modifier.fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Button(
                onClick = onComplete,
                modifier = Modifier
                    .fillMaxWidth(),
                enabled = isEnabled
            ) {
                Text(text = "Complete SetUp")
            }
        }
    }
}
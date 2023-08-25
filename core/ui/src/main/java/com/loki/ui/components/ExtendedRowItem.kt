package com.loki.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ExtendedRowItem(
    modifier: Modifier = Modifier,
    content: String,
    subContent: String,
    isEditable: Boolean = false,
    onRowClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                if (isEditable) {
                    onRowClick()
                }
            }
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 12.dp
            )
        ) {

            Column {
                Text(text = content, fontSize = 16.sp)
                Text(text = subContent, fontSize = 14.sp, color = MaterialTheme.colorScheme.onBackground.copy(.5f))
            }
            Spacer(modifier = Modifier.weight(1f))
            if (isEditable) {
                Icon(
                    imageVector = Icons.Filled.ArrowForwardIos,
                    contentDescription = "forward_icon"
                )
            }
        }
    }
}

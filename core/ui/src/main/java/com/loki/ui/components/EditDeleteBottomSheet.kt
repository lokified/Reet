package com.loki.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.twotone.DeleteForever
import androidx.compose.material.icons.twotone.EditNote
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDeleteBottomSheet(
    onDismiss: () -> Unit,
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit
) {

    ModalBottomSheet(
        onDismissRequest = onDismiss,
    ) {

        Column(
            modifier = Modifier.padding(
                start = 16.dp,
                end = 16.dp,
                bottom = 50.dp
            )
        ) {
            SheetRowItem(
                icon = Icons.TwoTone.EditNote,
                title = "Edit",
                onItemClick = onEditClick
            )
            SheetRowItem(
                icon = Icons.TwoTone.DeleteForever,
                title = "Delete",
                onItemClick = onDeleteClick
            )
        }

    }
}

@Composable
fun SheetRowItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    onItemClick: () -> Unit
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clickable { onItemClick() }
    ) {

        Icon(imageVector = icon, contentDescription = "$title icon")
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = title, fontSize = 16.sp)
    }
}

@Composable
fun EditTextFieldSheet(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    isEnabled: Boolean,
    isDarkTheme: Boolean,
    onComplete: () -> Unit,
    onDismiss: () -> Unit,
) {

    val containerColor = if (isDarkTheme) MaterialTheme.colorScheme.primary.copy(.2f)
        else MaterialTheme.colorScheme.primary.copy(.05f)

    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
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
                .align(Alignment.TopCenter)
                .padding(16.dp)
        ) {

            Text(
                text = "Edit Content",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground.copy(.5f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp)
                    .height(150.dp),
                enabled = isEnabled,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = containerColor,
                    unfocusedContainerColor = containerColor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = if (isDarkTheme) MaterialTheme.colorScheme.onBackground
                        else MaterialTheme.colorScheme.primary
                ),
            )
        }

        Button(
            onClick = onComplete,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            enabled = isEnabled
        ) {
            Text(text = "Edit")
        }
    }
}
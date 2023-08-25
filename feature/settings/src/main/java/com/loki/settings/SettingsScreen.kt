package com.loki.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.loki.ui.components.AppTopBar
import com.loki.ui.components.ExtendedRowItem

@Composable
fun SettingsScreen(
    navigateBack: () -> Unit,
    viewModel: SettingsViewModel
) {

    val appTheme = if (viewModel.isDarkTheme.value) "Dark" else "Light"
    var isAppThemeDialogVisible by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AppTopBar(
                leadingItem = {
                    IconButton(onClick = navigateBack) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                    }
                    Text(text = "Settings", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }
            )
        }
    ) { padding ->

        if (isAppThemeDialogVisible) {
            AppThemeDialog(
                isDarkTheme = viewModel.isDarkTheme.value,
                onDismiss = { isAppThemeDialogVisible = false },
                onSelected = { theme ->
                    val themeSelected = theme == "Dark"
                    viewModel.changeAppTheme(themeSelected)
                }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
        ) {

            ExtendedRowItem(
                content = "Change Theme",
                subContent = appTheme,
                isEditable = true,
                onRowClick = {
                    isAppThemeDialogVisible = true
                }
            )

        }
    }
}

@Composable
fun AppThemeDialog(
    isDarkTheme: Boolean,
    onDismiss: () -> Unit,
    onSelected: (String) -> Unit
) {

    val titles = listOf("Dark", "Light")
    val value = if (isDarkTheme) titles[0] else titles[1]
    var selected by remember { mutableStateOf(value) }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(
                    color = if (isDarkTheme) MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(12.dp)
                )
        ) {

            titles.forEach { title ->

                Row(
                    verticalAlignment = CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selected = title
                            onSelected(title)
                        }
                ) {
                    RadioButton(
                        selected = selected == title,
                        onClick = {
                            selected = title
                            onSelected(title)
                        },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.onBackground
                        )
                    )
                    Text(text = title)
                }
            }
        }
    }
}
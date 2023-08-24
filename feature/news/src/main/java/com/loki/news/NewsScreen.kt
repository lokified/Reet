package com.loki.news

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.loki.ui.components.AppTopBar
import com.loki.ui.components.NewsItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(
    viewModel: NewsViewModel
) {

    val uiState by viewModel.newsState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val openBrowser = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {}
    )

    if (uiState.errorMessage.isNotBlank()) {
        LaunchedEffect(key1 = uiState.errorMessage) {
            Toast.makeText(
                context,
                uiState.errorMessage,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                leadingItem = {
                    Text(text = "News", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                },
                modifier = Modifier.padding(16.dp)
            )
        }
    ) { padding ->

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        if (uiState.errorMessage == "check your internet connection") {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                TextButton(onClick = { viewModel.getNews() }) {
                    Text(text = "retry")
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            LazyColumn(contentPadding = PaddingValues(16.dp)) {

                items(uiState.newsList) { news ->

                    NewsItem(
                        news = news,
                        modifier = Modifier.padding(
                            vertical = 8.dp
                        )
                    ) { url ->

                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        openBrowser.launch(intent)
                    }
                }
            }
        }
    }
}
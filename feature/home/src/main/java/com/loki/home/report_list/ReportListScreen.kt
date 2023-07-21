package com.loki.home.report_list

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.loki.ui.components.AppTopBar
import com.loki.ui.components.Loading
import com.loki.ui.components.ProfileCircleBox
import com.loki.ui.components.ReportItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportListScreen(
    viewModel: ReportListViewModel,
    navigateToNewReport: () -> Unit,
    navigateToReport: (reportId: String) -> Unit
) {

    val uiState by viewModel.reportsUiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            AppTopBar(
                modifier = Modifier.padding(horizontal = 16.dp),
                leadingItem = {
                    ProfileCircleBox(
                        initials = viewModel.userInitial.value,
                        backgroundColor = Color(viewModel.localProfile.value.profileBackground),
                        initialsSize = 20,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Home", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = navigateToNewReport
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = null)
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {

            LazyColumn(contentPadding = PaddingValues(16.dp)) {

                items(uiState.matchedReport) { matchedReport ->

                    ReportItem(
                        matchedReport = matchedReport,
                        modifier = Modifier.padding(
                            vertical = 8.dp
                        ),
                        onItemClick = navigateToReport
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = .5.dp
                    )
                }
            }
        }

        if (uiState.matchedReport.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "No Reports Today", color = MaterialTheme.colorScheme.onBackground.copy(.5f))
            }
        }
    }

    if (uiState.isLoading) {
        Loading()
    }

    if (uiState.errorMessage.isNotBlank()) {
        LaunchedEffect(key1 = Unit) {
            Toast.makeText(
                context,
                uiState.errorMessage,
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
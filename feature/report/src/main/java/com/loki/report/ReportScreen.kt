package com.loki.report

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.loki.ui.components.AppTopBar
import com.loki.ui.components.CommentItem
import com.loki.ui.components.ProfileCircleBox
import com.loki.ui.utils.DateUtil.formatDate
import com.loki.ui.utils.DateUtil.formatTime
import com.loki.ui.utils.ext.toInitials

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    viewModel: ReportViewModel,
    onNavigateBack: () -> Unit
) {

    val uiState by viewModel.state
    val comments by viewModel.commentState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    if (viewModel.errorMessage.value.isNotBlank()) {
        LaunchedEffect(key1 = Unit) {
            Toast.makeText(
                context,
                viewModel.errorMessage.value,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {


        AppTopBar(
            leadingItem = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Arrow_back"
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Report", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
        )

        Box(modifier = Modifier.fillMaxSize()) {

            LazyColumn(
                contentPadding = PaddingValues(
                    bottom = 65.dp
                )
            ) {

                item {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        uiState.matchedReport?.profile?.let { profile ->
                            ProfileCircleBox(
                                initials = profile.name.toInitials(),
                                backgroundColor = Color(profile.profileBackgroundColor!!),
                                initialsSize = 20,
                                modifier = Modifier.size(40.dp)
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = profile.userName,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(imageVector = Icons.Filled.MoreHoriz, contentDescription = "more")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    uiState.matchedReport?.report?.let { report ->
                        Text(
                            text = report.reportContent,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )

                        report.reportImage?.let {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp, horizontal = 16.dp)
                                    .background(
                                        shape = RoundedCornerShape(8.dp),
                                        color = MaterialTheme.colorScheme.background
                                    )
                                    .height(200.dp)
                            ) {
                                AsyncImage(
                                    model = it,
                                    contentDescription = "report_image",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .shadow(
                                            shape = RoundedCornerShape(8.dp),
                                            elevation = 1.dp
                                        ),
                                    contentScale = ContentScale.Crop,
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(
                                horizontal = 16.dp,
                                vertical = 8.dp
                            )
                        ) {
                            Text(
                                text = "${report.createdAt!!.formatTime()} . ${report.createdOn!!.formatDate()}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onBackground.copy(.5f)
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            val commentText = if (comments.matchedComment.size > 1) "comments" else "comment"

                            Text(
                                text = "${comments.matchedComment.size} $commentText",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onBackground.copy(.5f)
                            )
                        }
                    }

                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = .5.dp
                    )
                }

                if (comments.matchedComment.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = ".")
                        }
                    }
                }

                items(comments.matchedComment) { matchedComment ->

                    CommentItem(
                        matchedComment = matchedComment,
                        onMoreClick = {},
                        modifier = Modifier.padding(
                            vertical = 4.dp,
                            horizontal = 16.dp
                        )
                    )
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = .5.dp
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                ) {

                    ProfileCircleBox(
                        initials = viewModel.localUser.value.name.toInitials(),
                        backgroundColor = Color(viewModel.localProfile.value.profileBackground),
                        initialsSize = 15,
                        modifier = Modifier.size(30.dp)
                    )

                    TextField(
                        value = uiState.commentContent,
                        onValueChange = viewModel::onCommentChange,
                        placeholder = {
                            Text(
                                text = "Add Comment",
                                color = MaterialTheme.colorScheme.onBackground.copy(.5f)
                            )
                        },
                        modifier = Modifier.width(250.dp),
                        enabled = !viewModel.isLoading.value,
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = MaterialTheme.colorScheme.primary.copy(.02f),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                        )
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = {
                            viewModel.addComment()
                        },
                        enabled = !viewModel.isLoading.value && uiState.commentContent.isNotBlank()
                    ) {
                        Text(text = "Comment")
                    }
                }
            }
        }
    }
}
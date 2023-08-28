package com.loki.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.loki.remote.model.MatchedComment
import com.loki.ui.utils.DateUtil.formatDate
import com.loki.ui.utils.DateUtil.formatTime
import com.loki.ui.utils.ext.toInitials

@Composable
fun CommentItem(
    modifier: Modifier = Modifier,
    matchedComment: MatchedComment,
    isUserMatched: Boolean,
    onMoreClick: () -> Unit
) {

    val profile = matchedComment.profile
    val comment = matchedComment.comment

    Box(
        modifier = modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            ProfileCircleBox(
                initials = profile.name.toInitials(),
                backgroundColor = Color(profile.profileBackgroundColor!!),
                initialsSize = 15,
                modifier = Modifier.size(30.dp)
            )

            Spacer(modifier = Modifier.width(4.dp))

            Column (
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = profile.userName, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = comment.createdAt!!.formatTime(),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp),
                        color = MaterialTheme.colorScheme.onBackground.copy(.5f)
                    )

                    if (isUserMatched) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "More",
                            modifier = Modifier.padding(4.dp)
                                .clickable { onMoreClick() }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(text = comment.commentContent)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = comment.createdOn!!.formatDate(),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(.5f)
                )
            }
        }
    }
}
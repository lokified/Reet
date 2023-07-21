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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.loki.remote.model.MatchedReport
import com.loki.ui.utils.DateUtil.formatDate
import com.loki.ui.utils.DateUtil.formatTime
import com.loki.ui.utils.ext.toInitials

@Composable
fun ReportItem(
    modifier: Modifier = Modifier,
    matchedReport: MatchedReport,
    onItemClick: (reportId: String) -> Unit
) {

    val report = matchedReport.report
    val profile = matchedReport.profile

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(report.id) }
    ) {
        Row(modifier = modifier.fillMaxWidth()) {
            ProfileCircleBox(
                initials = profile.name.toInitials(),
                backgroundColor = Color(profile.profileBackgroundColor!!),
                initialsSize = 15,
                modifier = Modifier.size(30.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = profile.userName, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = report.createdAt!!.formatTime(),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(.5f)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = report.reportContent)

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
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = report.createdOn!!.formatDate(),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(.5f)
                )
            }
        }
    }
}
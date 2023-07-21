package com.loki.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.loki.remote.news.model.News

@Composable
fun NewsItem(
    modifier: Modifier = Modifier,
    news: News,
    onReadMore: (url: String) -> Unit
) {

    val width = LocalConfiguration.current.screenWidthDp

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.onBackground.copy(.02f),
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Column {

            Text(
                text = news.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                modifier = Modifier.padding(8.dp)
            )

            Row(
                modifier = Modifier.padding(8.dp)
            ) {

                Text(
                    text = news.brief_description,
                    modifier = Modifier.width((width / 2).dp),
                    color = MaterialTheme.colorScheme.onBackground.copy(.5f)
                )

                Spacer(modifier = Modifier.width(4.dp))

                AsyncImage(
                    model = news.image_url,
                    contentDescription = "news_title",
                    modifier = Modifier
                        .fillMaxHeight().size(
                        height = 150.dp,
                        width = (width / 2).dp
                    ).clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.FillHeight
                )
            }

            Divider(
                modifier = Modifier.fillMaxWidth(),
                thickness = .5.dp
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                TextButton(onClick = { onReadMore(news.news_url) }) {
                    Text(text = "Read More")
                }
            }
        }

    }
}
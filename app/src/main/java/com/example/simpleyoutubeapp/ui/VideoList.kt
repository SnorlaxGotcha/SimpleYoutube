package com.example.simpleyoutubeapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter

@Composable
fun VideoList(videoDataList: List<VideoData>) {
    LazyColumn {
        items(videoDataList.size) { index ->
            VideoListItem(videoData = videoDataList[index])
        }
    }
}

@Composable
fun VideoListItem(videoData: VideoData) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        // Thumbnail
        Image(
            painter = rememberImagePainter(data = videoData.thumbnailUrl),
            contentDescription = "Thumbnail",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Row with avatar, video title, owner name, upload time
        Row(
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Avatar
            Image(
                painter = rememberImagePainter(data = videoData.avatarUrl),
                contentDescription = "Owner Avatar",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(text = videoData.videoTitle, style = MaterialTheme.typography.body1)
                Row {
                    Text(text = videoData.ownerName, style = MaterialTheme.typography.body2)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = videoData.uploadDatetime, style = MaterialTheme.typography.body2)
                }
            }
        }
    }
}
package com.example.simpleyoutubeapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun VideoList(
    videoDataList: List<VideoData>,
    onVideoClick: (VideoData) -> Unit,
    onLoadMore: () -> Unit,
    isLoading: Boolean // 新增 isLoading 參數來控制骨架屏
) {
    LazyColumn {
        if (isLoading) {
            // 顯示 5 個骨架屏項目
            items(5) {
                SkeletonVideoListItem()
            }
        } else {
            // 顯示實際的影片數據
            items(videoDataList) { video ->
                VideoListItem(videoData = video, onClick = { onVideoClick(video) })
            }
        }

        // 當滾動到底部時觸發 onLoadMore
        item {
            LaunchedEffect(Unit) {
                onLoadMore()
            }
        }
    }
}


@Composable
fun VideoListItem(videoData: VideoData, onClick: () -> Unit) {
    val formattedDate = formatUploadDatetime(videoData.uploadDatetime)

    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick() }
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
                    // 顯示格式化後的日期
                    Text(text = formattedDate, style = MaterialTheme.typography.body2)
                }
            }
        }
    }
}

// Helper function to format upload datetime
fun formatUploadDatetime(datetime: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = inputFormat.parse(datetime)
        date?.let {
            outputFormat.format(it)
        } ?: datetime
    } catch (e: Exception) {
        datetime // 如果格式化失敗，返回原始值
    }
}

@Composable
fun SkeletonVideoListItem() {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        // 模擬縮略圖
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color.LightGray)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 模擬標題和其他信息
        Row(
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // 模擬頭像
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(20.dp)
                        .background(Color.Gray)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(20.dp)
                        .background(Color.Gray)
                )
            }
        }
    }
}

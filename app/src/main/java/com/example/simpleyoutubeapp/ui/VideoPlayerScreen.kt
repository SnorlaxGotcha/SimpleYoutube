package com.example.simpleyoutubeapp.ui

import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.NavController

@Composable
fun VideoPlayerScreen(
    navController: NavController,  // 傳遞 NavController
    videoId: String,
    title: String,
    ownerName: String,
    uploadDatetime: String
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Video Player") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {  // 點擊返回按鈕時導航回上一頁
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                YouTubePlayer(videoId = videoId)

                Spacer(modifier = Modifier.height(16.dp))

                // 顯示影片資訊
                Text(text = title, style = MaterialTheme.typography.h6)
                Text(text = "Owner: $ownerName")
                Text(text = "Uploaded: $uploadDatetime")

                Spacer(modifier = Modifier.height(16.dp))

                // 顯示影片鏈接和其他內容
                Text(text = "https://www.youtube.com/watch?v=$videoId")
                Text(text = "Comments (optional)")
            }
        }
    )
}

@Composable
fun YouTubePlayer(videoId: String) {
    val context = LocalContext.current

    AndroidView(
        factory = {
            WebView(context).apply {
                // 啟用硬體加速
                setLayerType(WebView.LAYER_TYPE_HARDWARE, null)
                // 啟用 JavaScript
                settings.javaScriptEnabled = true
                // 允許影片全屏
                settings.mediaPlaybackRequiresUserGesture = false
                // 使用 WebViewClient 防止跳轉到外部瀏覽器
                webViewClient = WebViewClient()
            }
        },
        modifier = Modifier.fillMaxWidth().height(250.dp),
        update = {
            it.loadUrl("https://www.youtube.com/watch?v=$videoId")
        }
    )
}



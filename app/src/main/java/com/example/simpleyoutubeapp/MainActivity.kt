package com.example.simpleyoutubeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.ImeAction
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.simpleyoutubeapp.data.ChannelResponse
import com.example.simpleyoutubeapp.data.YouTubeApiService
import com.example.simpleyoutubeapp.data.PlaylistResponse
import com.example.simpleyoutubeapp.ui.VideoPlayerScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import kotlinx.coroutines.suspendCancellableCoroutine

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val retrofit = Retrofit.Builder()
    .baseUrl("https://www.googleapis.com/youtube/v3/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val service: YouTubeApiService = retrofit.create(YouTubeApiService::class.java)

suspend fun fetchChannelDetails(apiKey: String, channelId: String): Pair<String, String> {
    return suspendCancellableCoroutine { continuation ->
        val call = service.getChannelDetails("snippet", channelId, apiKey)
        call.enqueue(object : retrofit2.Callback<ChannelResponse> {
            override fun onResponse(call: retrofit2.Call<ChannelResponse>, response: retrofit2.Response<ChannelResponse>) {
                if (response.isSuccessful) {
                    val channel = response.body()?.items?.firstOrNull()
                    if (channel != null) {
                        val ownerName = channel.snippet.title
                        val avatarUrl = channel.snippet.thumbnails.default.url
                        continuation.resume(Pair(ownerName, avatarUrl), null)
                    } else {
                        continuation.resume(Pair("", ""), null)
                    }
                } else {
                    continuation.resume(Pair("", ""), null)
                }
            }

            override fun onFailure(call: retrofit2.Call<ChannelResponse>, t: Throwable) {
                continuation.resume(Pair("", ""), null)
            }
        })
    }
}

suspend fun fetchPlaylistItems(apiKey: String, playlistId: String, pageToken: String?): PlaylistResponse {
    return suspendCancellableCoroutine { continuation ->
        val call = service.getPlaylistItems(
            part = "snippet",
            playlistId = playlistId,
            apiKey = apiKey,
            pageToken = pageToken,
            maxResults = 30
        )
        call.enqueue(object : retrofit2.Callback<PlaylistResponse> {
            override fun onResponse(call: retrofit2.Call<PlaylistResponse>, response: retrofit2.Response<PlaylistResponse>) {
                if (response.isSuccessful) {
                    val items = response.body()?.items ?: emptyList()
                    val nextPageToken = response.body()?.nextPageToken
                    val videoDataList = items.map { item ->
                        VideoData(
                            videoId = item.snippet.resourceId.videoId,
                            thumbnailUrl = item.snippet.thumbnails.default.url,
                            avatarUrl = "", // 暫時設置
                            videoTitle = item.snippet.title,
                            ownerName = "", // 暫時設置
                            uploadDatetime = item.snippet.publishedAt
                        )
                    }
                    continuation.resume(PlaylistResponse(items, nextPageToken), null)
                } else {
                    continuation.resume(PlaylistResponse(emptyList(), null), null)
                }
            }

            override fun onFailure(call: retrofit2.Call<PlaylistResponse>, t: Throwable) {
                continuation.resume(PlaylistResponse(emptyList(), null), null)
            }
        })
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApp(apiKey = "AIzaSyD3KIzJyPunDkE-9tNQP4cerP79-yNpTt0")
        }
    }
}

@Composable
fun MyApp(apiKey: String) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "videoList") {
        composable("videoList") {
            HomeScreen(apiKey, navController)
        }
        composable(
            route = "videoPlayer/{videoId}/{title}/{ownerName}/{uploadDatetime}",
            arguments = listOf(
                navArgument("videoId") { type = NavType.StringType },
                navArgument("title") { type = NavType.StringType },
                navArgument("ownerName") { type = NavType.StringType },
                navArgument("uploadDatetime") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val videoId = backStackEntry.arguments?.getString("videoId") ?: ""
            val title = backStackEntry.arguments?.getString("title") ?: ""
            val ownerName = backStackEntry.arguments?.getString("ownerName") ?: ""
            val uploadDatetime = backStackEntry.arguments?.getString("uploadDatetime") ?: ""

            VideoPlayerScreen(
                navController = navController,  // 傳遞 navController
                videoId = videoId,
                title = title,
                ownerName = ownerName,
                uploadDatetime = uploadDatetime
            )
        }
    }
}

@Composable
fun HomeScreen(apiKey: String, navController: NavController) {
    var videoDataList by remember { mutableStateOf<List<VideoData>>(emptyList()) }
    var nextPageToken by remember { mutableStateOf<String?>(null) }
    var ownerName by remember { mutableStateOf("") }
    var avatarUrl by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }  // 初始狀態設為 true，表示正在加載數據

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        // 獲取頻道詳細信息
        val (name, avatar) = fetchChannelDetails(apiKey, "UCMUnInmOkrWN4gof9KlhNmQ")
        ownerName = name
        avatarUrl = avatar

        // 模擬一個延遲來顯示 Skeleton
        delay(2000)

        val response = fetchPlaylistItems(apiKey, "UUMUnInmOkrWN4gof9KlhNmQ", null)
        videoDataList = response.items.map { playlistItem ->
            VideoData(
                videoId = playlistItem.snippet.resourceId.videoId,
                thumbnailUrl = playlistItem.snippet.thumbnails.default.url,
                avatarUrl = avatarUrl,
                videoTitle = playlistItem.snippet.title,
                ownerName = ownerName,
                uploadDatetime = playlistItem.snippet.publishedAt
            )
        }
        nextPageToken = response.nextPageToken
        isLoading = false  // 數據加載完成後，將加載狀態設為 false，隱藏 Skeleton
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isLoading) "Loading..." else ownerName) },
                actions = {
                    IconButton(onClick = { /* 搜索邏輯 */ }) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                    }
                }
            )
        },
        content = {
            Column(modifier = Modifier.fillMaxSize()) {
                // 根據加載狀態顯示骨架屏或真實數據
                VideoList(
                    videoDataList = videoDataList,
                    onVideoClick = { video ->
                        navController.navigate(
                            "videoPlayer/${video.videoId}/${video.videoTitle}/${video.ownerName}/${video.uploadDatetime}"
                        )
                    },
                    onLoadMore = {
                        if (!isLoading && nextPageToken != null) {
                            isLoading = true
                            // 使用協程範圍來加載更多數據
                            coroutineScope.launch {
                                val response = fetchPlaylistItems(apiKey, "UUMUnInmOkrWN4gof9KlhNmQ", nextPageToken)
                                videoDataList = videoDataList + response.items.map { playlistItem ->
                                    VideoData(
                                        videoId = playlistItem.snippet.resourceId.videoId,
                                        thumbnailUrl = playlistItem.snippet.thumbnails.default.url,
                                        avatarUrl = avatarUrl,
                                        videoTitle = playlistItem.snippet.title,
                                        ownerName = ownerName,
                                        uploadDatetime = playlistItem.snippet.publishedAt
                                    )
                                }
                                nextPageToken = response.nextPageToken
                                isLoading = false
                            }
                        }
                    },
                    isLoading = isLoading // 傳遞加載狀態，控制骨架屏顯示
                )
            }
        }
    )
}


@Composable
fun SearchBar(
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    onFocusChange: (Boolean) -> Unit
) {
    TextField(
        value = searchQuery,
        onValueChange = onQueryChange,
        placeholder = { Text(text = "Search...") },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .onFocusChanged { focusState -> onFocusChange(focusState.isFocused) },
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                // Handle search action here
            }
        )
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApp(apiKey = "AIzaSyD3KIzJyPunDkE-9tNQP4cerP79-yNpTt0")
}
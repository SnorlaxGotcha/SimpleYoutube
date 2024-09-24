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
import com.example.simpleyoutubeapp.data.ChannelResponse
import com.example.simpleyoutubeapp.data.YouTubeApiService
import com.example.simpleyoutubeapp.data.YouTubeResponse

import com.example.simpleyoutubeapp.ui.theme.SimpleYoutubeAppTheme
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

suspend fun fetchPlaylistItems(apiKey: String, playlistId: String): List<VideoData> {
    return suspendCancellableCoroutine { continuation ->
        val call = service.getPlaylistItems("snippet", playlistId, apiKey, 50) // Max現在上限50
        call.enqueue(object : retrofit2.Callback<YouTubeResponse> {
            override fun onResponse(call: retrofit2.Call<YouTubeResponse>, response: retrofit2.Response<YouTubeResponse>) {
                if (response.isSuccessful) {
                    val items = response.body()?.items ?: emptyList()
                    val videoDataList = items.map { item ->
                        VideoData(
                            thumbnailUrl = item.snippet.thumbnails.default.url,
                            avatarUrl = "",  // 這裡暫時設為空，之後會用 fetchChannelDetails 來替換
                            videoTitle = item.snippet.title,
                            ownerName = "",  // 這裡暫時設為空，之後會用 fetchChannelDetails 來替換
                            uploadDatetime = item.snippet.publishedAt
                        )
                    }
                    continuation.resume(videoDataList, null)
                } else {
                    continuation.resume(emptyList(), null)
                }
            }

            override fun onFailure(call: retrofit2.Call<YouTubeResponse>, t: Throwable) {
                continuation.resume(emptyList(), null)
            }
        })
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApp {
//                VideoList(videoDataList = sampleVideoData())
                HomeScreen("")
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            content()
        }
    }
}

@Composable
fun HomeScreen(apiKey: String) {
    var videoDataList by remember { mutableStateOf<List<VideoData>>(emptyList()) }
    var ownerName by remember { mutableStateOf("") }
    var avatarUrl by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        // Step 1, 首先獲取 Channel Details
        val (name, avatar) = fetchChannelDetails(apiKey, "UC0C-w0YjGpqDXGB8IHb662A")
        ownerName = name
        avatarUrl = avatar

        // Step 2, 接著獲取 Playlist Items，並更新 ownerName 和 avatarUrl
        val videos = fetchPlaylistItems(apiKey, "UUMUnInmOkrWN4gof9KlhNmQ").map { video ->
            video.copy(ownerName = ownerName, avatarUrl = avatarUrl)
        }
        videoDataList = videos
    }

    var searchQuery by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = if (isFocused) "" else "Simple Youtube")
                },
                actions = {
                    IconButton(onClick = { /* Handle search icon click if necessary */ }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Icon"
                        )
                    }
                }
            )
        },
        content = {
            Column(modifier = Modifier.fillMaxSize().padding(it)) {
                SearchBar(
                    searchQuery = searchQuery,
                    onQueryChange = { query -> searchQuery = query },
                    onFocusChange = { focused -> isFocused = focused }
                )
                Spacer(modifier = Modifier.height(16.dp))
//                VideoList(videoDataList = sampleVideoData())
                VideoList(videoDataList = videoDataList)
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
    MyApp {
        HomeScreen("")
    }
}
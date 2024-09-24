package com.example.simpleyoutubeapp

data class VideoData(
    val thumbnailUrl: String,
    val avatarUrl: String,
    val videoTitle: String,
    val ownerName: String,
    val uploadDatetime: String
)

fun sampleVideoData(): List<VideoData> {
    return listOf(
        VideoData(
            thumbnailUrl = "https://via.placeholder.com/300.png",
            avatarUrl = "https://via.placeholder.com/150.png",
            videoTitle = "Sample Video 1",
            ownerName = "Owner 1",
            uploadDatetime = "2023-09-20"
        ),
        VideoData(
            thumbnailUrl = "https://via.placeholder.com/300.png",
            avatarUrl = "https://via.placeholder.com/150.png",
            videoTitle = "Sample Video 2",
            ownerName = "Owner 2",
            uploadDatetime = "2023-09-21"
        )
        // Add more items as needed
    )
}

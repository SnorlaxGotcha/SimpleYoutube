package com.example.simpleyoutubeapp

data class VideoData(
    val videoId: String,
    val thumbnailUrl: String,
    val avatarUrl: String,
    val videoTitle: String,
    val ownerName: String,
    val uploadDatetime: String
)
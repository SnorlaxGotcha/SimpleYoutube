package com.example.simpleyoutubeapp.data

data class YouTubeResponse(
    val items: List<PlaylistItem>
)

data class PlaylistItem(
    val snippet: Snippet
)

data class Snippet(
    val title: String,
    val description: String,
    val thumbnails: Thumbnails,
    val resourceId: ResourceId,
    val publishedAt: String
)

data class Thumbnails(
    val default: ThumbnailDetails
)

data class ThumbnailDetails(
    val url: String
)

data class ResourceId(
    val videoId: String
)


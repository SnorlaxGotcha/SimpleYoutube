package com.example.simpleyoutubeapp.data

data class ChannelResponse(
    val items: List<ChannelItem>
)

data class ChannelItem(
    val snippet: ChannelSnippet
)

data class ChannelSnippet(
    val title: String,         // 頻道名稱
    val thumbnails: AvatarThumbnails // 頻道頭像
)

data class AvatarThumbnails(
    val default: AvatarThumbnailDetails
)

data class AvatarThumbnailDetails(
    val url: String // 頭像的 URL
)

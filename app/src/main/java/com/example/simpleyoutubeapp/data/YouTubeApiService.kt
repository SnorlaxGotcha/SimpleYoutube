package com.example.simpleyoutubeapp.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface YouTubeApiService {
    @GET("channels")
    fun getChannelDetails(
        @Query("part") part: String,
        @Query("id") channelId: String,
        @Query("key") apiKey: String
    ): Call<ChannelResponse>

    @GET("playlistItems")
    fun getPlaylistItems(
        @Query("part") part: String,
        @Query("playlistId") playlistId: String,
        @Query("key") apiKey: String,
        @Query("maxResults") maxResults: Int
    ): Call<YouTubeResponse>
}

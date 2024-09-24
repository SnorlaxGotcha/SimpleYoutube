package com.example.simpleyoutubeapp

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testVideoDataInitialization() {
        val videoData = VideoData(
            videoId = "abc123",
            thumbnailUrl = "http://example.com/thumbnail.jpg",
            avatarUrl = "http://example.com/avatar.jpg",
            videoTitle = "Sample Video",
            ownerName = "Sample Owner",
            uploadDatetime = "2021-10-01T12:00:00Z"  // ISO 8601 格式
        )

        // 格式化日期
        val formattedDate = formatUploadDatetime(videoData.uploadDatetime)

        // 檢查日期格式是否正確
        assertEquals("2021-10-01 12:00:00", formattedDate)

        // 驗證 videoData 初始化是否正確
        assertEquals("abc123", videoData.videoId)
        assertEquals("Sample Video", videoData.videoTitle)
        assertEquals("Sample Owner", videoData.ownerName)
        assertEquals("2021-10-01T12:00:00Z", videoData.uploadDatetime)
    }

    @Test
    fun testEmptyVideoDataList() {
        val videoDataList: List<VideoData> = emptyList()

        // 驗證當 videoDataList 為空時的行為
        assertEquals(0, videoDataList.size)
    }

}
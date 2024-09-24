package com.example.simpleyoutubeapp

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.simpleyoutubeapp", appContext.packageName)
    }

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testSkeletonViewDisplayedWhenLoading() {
        composeTestRule.setContent {
            VideoList(
                videoDataList = emptyList(),
                onVideoClick = {},
                onLoadMore = {},
                isLoading = true  // 模擬數據加載狀態
            )
        }

        // 確認Skeleton是否顯示
        composeTestRule.onNodeWithText("Loading...").assertExists()
    }

    @Test
    fun testLazyLoadTriggered() {
        var loadMoreTriggered = false
        composeTestRule.setContent {
            VideoList(
                videoDataList = listOf(VideoData(
                    videoId = "abc123",
                    thumbnailUrl = "http://example.com/thumbnail.jpg",
                    avatarUrl = "http://example.com/avatar.jpg",
                    videoTitle = "Sample Video",
                    ownerName = "Sample Owner",
                    uploadDatetime = "2021-10-01"
                )),
                onVideoClick = {},
                onLoadMore = {
                    loadMoreTriggered = true
                },
                isLoading = false
            )
        }

        // 模擬滾動到底部，觸發lazy loading
        composeTestRule.onNodeWithText("Sample Video").performClick()

        assertEquals(true, loadMoreTriggered)
    }
}
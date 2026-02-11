package com.example.moviesandmore

import com.example.moviesandmore.data.MovieApiService
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class MovieIntegrationTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private lateinit var mockWebServer: MockWebServer

    @Inject
    lateinit var apiService: MovieApiService

    @Before
    fun init() {
        mockWebServer = MockWebServer()
        mockWebServer.start(8080)
        hiltRule.inject()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testNetworkCallToMockServer() = runTest {
        val json = """{
            "titles": [{"id": "1", "primaryTitle": "Interstellar"}],
            "totalCount": 1,
            "nextPageToken": null
        }"""

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(json)
                .addHeader("Content-Type", "application/json")
        )

        val response = apiService.getTitles()

        assert(response.titles.isNotEmpty())
        assert(response.titles[0].primaryTitle == "Interstellar")
    }
}
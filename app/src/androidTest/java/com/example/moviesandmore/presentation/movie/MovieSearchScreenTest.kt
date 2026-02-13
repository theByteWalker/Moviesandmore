package com.example.moviesandmore.presentation.movie

import android.content.Context
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.moviesandmore.MainActivity
import com.example.moviesandmore.data.local.AppDatabase
import com.example.moviesandmore.data.movie.MovieApiService
import com.example.moviesandmore.data.movie.MovieDao
import com.example.moviesandmore.data.movie.MovieModule
import com.example.moviesandmore.data.movie.MovieMapper
import com.example.moviesandmore.data.movie.MovieRepositoryImpl
import com.example.moviesandmore.di.DatabaseModule
import com.example.moviesandmore.domain.movie.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@UninstallModules(MovieModule::class, DatabaseModule::class)
@HiltAndroidTest
class MovieSearchScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        hiltRule.inject()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Module
    @InstallIn(SingletonComponent::class)
    object TestMovieModule {

        @Provides
        @Singleton
        fun provideRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl("http://localhost:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        @Provides
        @Singleton
        fun provideMovieApiService(retrofit: Retrofit): MovieApiService {
            return retrofit.create(MovieApiService::class.java)
        }

        @Provides
        @Singleton
        fun provideMovieMapper(): MovieMapper {
            return MovieMapper()
        }

        @Provides
        @Singleton
        fun provideMovieRepository(
            movieApiService: MovieApiService,
            movieMapper: MovieMapper,
            movieDao: MovieDao
        ): MovieRepository {
            return MovieRepositoryImpl(movieApiService, movieMapper, movieDao)
        }


    }

    @Module
    @InstallIn(SingletonComponent::class)
    class TestDatabaseModule {

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Create a new table with the correct schema
                db.execSQL("""
                CREATE TABLE IF NOT EXISTS movies_new (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    title_id TEXT NOT NULL DEFAULT '',
                    name TEXT NOT NULL,
                    poster_url TEXT,
                    is_favorite INTEGER NOT NULL DEFAULT 0
                )
            """.trimIndent())

                // Copy data from old table to new table
                db.execSQL("""
                INSERT INTO movies_new (id, name, poster_url, title_id, is_favorite)
                SELECT id, name, poster_url, '', 0
                FROM movies
            """.trimIndent())

                // Drop old table
                db.execSQL("DROP TABLE movies")

                // Rename new table to movies
                db.execSQL("ALTER TABLE movies_new RENAME TO movies")
            }
        }

        @Provides
        @Singleton
        fun provideDatabase(
            @ApplicationContext context: Context
        ): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "app_database"
            )
                .addMigrations(MIGRATION_1_2)
                .build()
        }

        @Provides
        @Singleton
        fun provideMovieDao(database: AppDatabase): MovieDao {
            return database.movieDao()
        }
    }


    @Test
    fun search_displays_movie_from_json() {
        Thread.sleep(1000)
        val jsonResponse = """
            {
              "titles": [
                {
                  "id": "tt1375666",
                  "primaryTitle": "Inception",
                  "primaryImage": {
                    "url": "https://example.com/inception.jpg"
                  }
                }
              ]
            }
        """.trimIndent()

        mockWebServer.enqueue(MockResponse().setBody(jsonResponse).setResponseCode(200))

        mockWebServer.shutdown()
        mockWebServer = MockWebServer()
        mockWebServer.start(8080)

        Thread.sleep(2000)


        composeRule.onNodeWithTag("search_bar").performTextInput("Inception")

        Thread.sleep(2000)
        
        composeRule.waitUntil(timeoutMillis = 5000) {
            try {
                composeRule.onNodeWithText("Inception").assertIsDisplayed()
                true
            } catch (e: AssertionError) {
                false
            }
        }

        // Assert
        composeRule.onNodeWithText("Inception").assertIsDisplayed()

        Thread.sleep(5000)
    }
}

package com.example.moviesandmore.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.moviesandmore.data.local.AppDatabase
import com.example.moviesandmore.data.movie.MovieDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

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
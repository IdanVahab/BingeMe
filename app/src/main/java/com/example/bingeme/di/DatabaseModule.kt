package com.example.bingeme.di

import android.content.Context
import androidx.room.Room
import com.example.bingeme.data.local.AppDatabase
import com.example.bingeme.data.local.dao.WatchlistDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Provides dependencies related to the Room database using Dagger-Hilt.
 * This module ensures the database and its DAOs are available for dependency injection.
 */
@Module
@InstallIn(SingletonComponent::class) // Specifies the module's scope to the entire application
object DatabaseModule {

    /**
     * Provides a singleton instance of the AppDatabase.
     * The database is configured with fallbackToDestructiveMigration to handle schema changes.
     *
     * @param context The application context, injected by Hilt.
     * @return A configured AppDatabase instance.
     */
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "bingeme_database" // Name of the database file
        )
            .fallbackToDestructiveMigration() // Automatically handle database version changes
            .build()
    }

    /**
     * Provides the WatchlistDao instance for accessing the watchlist-related database operations.
     *
     * @param appDatabase The AppDatabase instance from which the DAO is retrieved.
     * @return An instance of WatchlistDao.
     */
    @Provides
    fun provideWatchlistDao(appDatabase: AppDatabase): WatchlistDao {
        return appDatabase.watchlistDao()
    }
}

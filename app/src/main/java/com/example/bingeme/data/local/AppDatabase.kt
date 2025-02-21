package com.example.bingeme.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.bingeme.data.local.dao.MediaDao
import com.example.bingeme.data.local.entities.MovieEntity
import com.example.bingeme.data.local.entities.SeriesEntity

/**
 * Represents the Room database for the application.
 * This abstract class defines the database configuration and serves as the main access point
 * for the underlying SQLite database connection.
 *
 * @entities Specifies the list of entity classes that represent database tables.
 * @version Database schema version. Increment this number when schema changes.
 */
@Database(entities = [MovieEntity::class, SeriesEntity::class], version = 3)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Provides access to the WatchlistDao for performing operations on the "movies" and "series" tables.
     *
     * @return An instance of WatchlistDao.
     */

    abstract fun watchlistDao(): MediaDao
}

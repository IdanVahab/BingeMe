package com.example.bingeme.data.local.dao

import androidx.room.*
import com.example.bingeme.data.local.entities.MovieEntity
import com.example.bingeme.data.local.entities.SeriesEntity
import kotlinx.coroutines.flow.Flow

/**
 * The WatchlistDao interface defines database operations for managing
 * movies and series in the user's watchlist. This includes adding,
 * deleting, and querying items from the local Room database.
 */
@Dao
interface MediaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity)

    @Delete
    suspend fun deleteMovie(movie: MovieEntity)

    @Query("SELECT * FROM movies")
    fun getAllMovies(): Flow<List<MovieEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM movies WHERE id = :movieId AND isFavorite = 1)")
    suspend fun isMovieInWatchlist(movieId: Int): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM movies WHERE id = :movieId AND isWatched = 1)")
    suspend fun isMovieWatched(movieId: Int): Boolean

    @Query("SELECT * FROM movies WHERE isWatched = 1")
    fun getWatchedMovies(): Flow<List<MovieEntity>>

    @Query("UPDATE movies SET isWatched = :watched WHERE id = :movieId")
    suspend fun updateMovieWatchedStatus(movieId: Int, watched: Boolean)





    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeries(series: SeriesEntity)

    @Delete
    suspend fun deleteSeries(series: SeriesEntity)

    @Query("SELECT * FROM series")
    fun getAllSeries(): Flow<List<SeriesEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM series WHERE id = :seriesId)")
    suspend fun isSeriesInWatchlist(seriesId: Int): Boolean

    @Query("SELECT * FROM series WHERE isWatched = 1")
    fun getWatchedSeries(): Flow<List<SeriesEntity>>

    @Query("UPDATE series SET isWatched = :watched WHERE id = :seriesId")
    suspend fun updateSeriesWatchedStatus(seriesId: Int, watched: Boolean)

    @Query("SELECT isWatched FROM series WHERE id = :seriesId LIMIT 1")
    suspend fun isSeriesWatched(seriesId: Int): Boolean
}

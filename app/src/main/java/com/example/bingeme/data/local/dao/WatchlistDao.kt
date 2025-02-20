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
interface WatchlistDao {

    /**
     * Inserts a movie into the watchlist. If a movie with the same ID already exists,
     * it replaces the old entry.
     *
     * @param movie The movie entity to be added.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity)

    /**
     * Deletes a movie from the watchlist.
     *
     * @param movie The movie entity to be removed.
     */
    @Delete
    suspend fun deleteMovie(movie: MovieEntity)

    /**
     * Retrieves all movies in the watchlist as a Flow, which allows observing
     * real-time updates to the list.
     *
     * @return A Flow emitting the list of MovieEntity objects in the watchlist.
     */
    @Query("SELECT * FROM movies")
    fun getAllMovies(): Flow<List<MovieEntity>>

    /**
     * Checks if a specific movie is in the watchlist.
     *
     * @param movieId The ID of the movie to check.
     * @return True if the movie is in the watchlist, false otherwise.
     */
    @Query("SELECT EXISTS(SELECT 1 FROM movies WHERE id = :movieId)")
    suspend fun isMovieInWatchlist(movieId: Int): Boolean

    /**
     * Inserts a series into the watchlist. If a series with the same ID already exists,
     * it replaces the old entry.
     *
     * @param series The series entity to be added.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeries(series: SeriesEntity)

    /**
     * Deletes a series from the watchlist.
     *
     * @param series The series entity to be removed.
     */
    @Delete
    suspend fun deleteSeries(series: SeriesEntity)

    /**
     * Retrieves all series in the watchlist as a List.
     *
     * @return A List of SeriesEntity objects in the watchlist.
     */
    @Query("SELECT * FROM series")
    fun getAllSeries(): Flow<List<SeriesEntity>>

    /**
     * Checks if a specific series is in the watchlist.
     *
     * @param seriesId The ID of the series to check.
     * @return True if the series
     * is in the watchlist, false otherwise.
     */
    @Query("SELECT EXISTS(SELECT 1 FROM series WHERE id = :seriesId)")
    suspend fun isSeriesInWatchlist(seriesId: Int): Boolean

    @Query("SELECT * FROM movies WHERE isWatched = 1")
    fun getWatchedMovies(): Flow<List<MovieEntity>>

    @Query("UPDATE movies SET isWatched = :watched WHERE id = :movieId")
    suspend fun updateMovieWatchedStatus(movieId: Int, watched: Boolean)

    @Query("SELECT * FROM series WHERE isWatched = 1")
    fun getWatchedSeries(): Flow<List<SeriesEntity>>

    @Query("UPDATE series SET isWatched = :watched WHERE id = :seriesId")
    suspend fun updateSeriesWatchedStatus(seriesId: Int, watched: Boolean)

    @Query("SELECT isWatched FROM movies WHERE id = :movieId")
    suspend fun isMovieWatched(movieId: Int): Boolean

    @Query("SELECT isWatched FROM series WHERE id = :seriesId LIMIT 1")
    suspend fun isSeriesWatched(seriesId: Int): Boolean
}

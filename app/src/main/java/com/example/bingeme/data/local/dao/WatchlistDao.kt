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
    suspend fun getAllSeries(): List<SeriesEntity>

    /**
     * Checks if a specific movie is in the watchlist.
     *
     * @param movieId The ID of the movie to check.
     * @return True if the movie is in the watchlist, false otherwise.
     */
    @Query("SELECT EXISTS(SELECT 1 FROM movies WHERE id = :movieId)")
    suspend fun isMovieInWatchlist(movieId: Int): Boolean
}

package com.example.bingeme.domain.repositories

import android.util.Log
import com.example.bingeme.data.local.dao.WatchlistDao
import com.example.bingeme.data.local.entities.MovieEntity
import com.example.bingeme.data.local.entities.SeriesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Repository class for managing watchlist-related operations.
 * This class acts as an intermediary between the WatchlistDao and the rest of the application.
 *
 * @param dao Injected DAO for accessing watchlist-related database operations.
 */
class WatchlistRepository @Inject constructor(
    private val dao: WatchlistDao
) {

    /**
     * Adds a movie to the watchlist.
     *
     * @param movieEntity The movie entity to add.
     */
    suspend fun addMovie(movieEntity: MovieEntity) {
        dao.insertMovie(movieEntity)
    }

    /**
     * Removes a movie from the watchlist.
     *
     * @param movieEntity The movie entity to remove.
     */
    suspend fun removeMovie(movieEntity: MovieEntity) {
        dao.deleteMovie(movieEntity)
    }

    /**
     * Retrieves all movies from the watchlist as a Flow.
     * This allows observing changes in real-time.
     *
     * @return A Flow emitting a list of MovieEntity objects.
     */
    fun getAllMovies(): Flow<List<MovieEntity>> {
        return dao.getAllMovies()
    }

    /**
     * Checks if a specific movie is in the watchlist.
     *
     * @param movieId The ID of the movie to check.
     * @return True if the movie exists in the watchlist, false otherwise.
     */
    suspend fun isMovieInWatchlist(movieId: Int): Boolean {
        val exists = dao.isMovieInWatchlist(movieId)
        Log.d("WatchlistRepository", "Checking if movie ($movieId) is in watchlist: $exists")
        return exists
    }

    /**
     * Adds a series to the watchlist.
     *
     * @param series The series entity to add.
     */
    suspend fun addSeries(series: SeriesEntity) {
        Log.d("WatchlistRepository", "Adding series to watchlist: ${series.id} ")
        dao.insertSeries(series)
    }

    /**
     * Removes a series from the watchlist.
     *
     * @param series The series entity to remove.
     */
    suspend fun removeSeries(series: SeriesEntity) = dao.deleteSeries(series)

    /**
     * Retrieves all series from the watchlist as a Flow.
     * This allows observing changes in real-time.
     *
     * @return A Flow emitting a list of SeriesEntity objects.
     */
    fun getAllSeries(): Flow<List<SeriesEntity>> {
        return dao.getAllSeries().also {
            Log.d("WatchlistRepository", "Fetching watchlist series from DB")
        }
    }


    /**
     * Checks if a specific series is in the watchlist.
     *
     * @param seriesId The ID of the series to check.
     * @return True if the series exists in the watchlist, false otherwise.
     */
    suspend fun isSeriesInWatchlist(seriesId: Int): Boolean {
        val exists = dao.isSeriesInWatchlist(seriesId)
        Log.d("WatchlistRepository", "Checking if series ($seriesId) is in watchlist: $exists")
        return exists
    }

}

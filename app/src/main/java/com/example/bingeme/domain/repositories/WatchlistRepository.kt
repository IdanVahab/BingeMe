package com.example.bingeme.domain.repositories

import com.example.bingeme.data.local.dao.WatchlistDao
import com.example.bingeme.data.local.entities.MovieEntity
import com.example.bingeme.data.local.entities.SeriesEntity
import com.example.bingeme.data.models.Movie
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
        return dao.isMovieInWatchlist(movieId)
    }

    /**
     * Adds a series to the watchlist.
     *
     * @param series The series entity to add.
     */
    suspend fun addSeries(series: SeriesEntity) = dao.insertSeries(series)

    /**
     * Removes a series from the watchlist.
     *
     * @param series The series entity to remove.
     */
    suspend fun removeSeries(series: SeriesEntity) = dao.deleteSeries(series)

    /**
     * Retrieves all series from the watchlist.
     *
     * @return A list of SeriesEntity objects.
     */
    suspend fun getSeries() = dao.getAllSeries()
}

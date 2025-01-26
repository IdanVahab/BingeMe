package com.example.bingeme.domain.repositories

import com.example.bingeme.data.local.dao.WatchlistDao
import com.example.bingeme.data.local.entities.MovieEntity
import com.example.bingeme.data.models.Movie
import com.example.bingeme.data.remote.TmdbApiService
import com.example.bingeme.utils.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Repository class for managing movie-related data.
 * This class acts as a mediator between the data sources (API and local database)
 * and the rest of the application.
 *
 * @param apiService Injected TMDB API service for fetching movie data.
 * @param watchlistDao Injected DAO for accessing watchlist-related database operations.
 */
class MoviesRepository @Inject constructor(
    private val apiService: TmdbApiService,
    private val watchlistDao: WatchlistDao
) {

    /**
     * Fetches a list of popular movies from the TMDB API.
     *
     * @param apiKey The API key for authentication.
     * @return A response containing the popular movies.
     */
    suspend fun getPopularMovies(apiKey: String) = apiService.getPopularMovies(apiKey)

    /**
     * Fetches details of a specific movie from the TMDB API.
     *
     * @param apiKey The API key for authentication.
     * @param movieId The ID of the movie to fetch details for.
     * @return A response containing the movie details.
     */
    suspend fun getMovieDetails(apiKey: String, movieId: Int) =
        apiService.getMovieDetails(movieId, apiKey)

    /**
     * Adds a movie to the watchlist if it is not already present.
     *
     * @param movie The movie entity to add to the watchlist.
     */
    suspend fun addMovieToWatchlist(movie: MovieEntity) {
        if (!watchlistDao.isMovieInWatchlist(movie.id)) {
            watchlistDao.insertMovie(movie)
        }
    }

    /**
     * Removes a movie from the watchlist.
     *
     * @param movie The movie entity to remove from the watchlist.
     */
    suspend fun removeMovieFromWatchlist(movie: MovieEntity) {
        watchlistDao.deleteMovie(movie)
    }

    /**
     * Retrieves all movies from the watchlist.
     * The result is provided as a Flow for observing changes in real-time.
     *
     * @return A Flow emitting a list of movies in the watchlist.
     */
    fun getAllMoviesFromWatchlist(): Flow<List<Movie>> {
        return watchlistDao.getAllMovies().map { entities ->
            entities.map { it.toModel() }
        }
    }
}

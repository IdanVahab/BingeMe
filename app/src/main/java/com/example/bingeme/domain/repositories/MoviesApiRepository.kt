package com.example.bingeme.domain.repositories

import com.example.bingeme.data.local.dao.MediaDao
import com.example.bingeme.data.models.Movie
import com.example.bingeme.data.models.MovieResponse
import com.example.bingeme.data.remote.TmdbApiService
import com.example.bingeme.utils.Constants
import retrofit2.Response
import javax.inject.Inject

/**
 * Repository class for managing movie-related data.
 * This class acts as a mediator between the data sources (API and local database)
 * and the rest of the application.
 *
 * @param apiService Injected TMDB API service for fetching movie data.
 * @param watchlistDao Injected DAO for accessing watchlist-related database operations.
 */
class MoviesApiRepository @Inject constructor(
    override val apiService: TmdbApiService,
    private val mediaDao: MediaDao
) : TmdbApiBaseRepository(apiService) {

    suspend fun getPopularMovies(apiKey: String, page: Int = 1): Response<MovieResponse> {
        val response = apiService.getPopularMovies(apiKey, page = page)

        if (!response.isSuccessful) {
            return response
        }

        val movieList = response.body()?.results ?: emptyList()

        // ✅ Fetch all favorite and watched movies IDs in a single query
        val favoriteIds = mediaDao.getFavoriteMoviesIds()
        val watchedIds = mediaDao.getWatchedMoviesIds()

        movieList.forEach { movie ->
            movie.isFavorite = movie.id in favoriteIds
            movie.isWatched = movie.id in watchedIds
        }

        val updatedResponse = response.body()?.copy(results = movieList)

        return Response.success(updatedResponse)
    }


    suspend fun getMovieDetails(apiKey: String, token: String, movieId: Int): Response<Movie> {
        val response: Response<Movie> = apiService.getMovieDetails(movieId, apiKey)

        if (response.isSuccessful) {
            response.body()?.let { movie ->
                // Fetch the trailer URL
                val trailerUrl = getTrailerUrl(token, movieId, isMovie = true)

                // Update the movie object with the trailer URL
                val updatedMovie = movie.copy(trailerUrl = trailerUrl)

                // Return the updated Response<Movie>
                return Response.success(updatedMovie)
            }
        }
        return response // Return the original response if unsuccessful
    }

    suspend fun searchMovies(query: String): Response<MovieResponse> {
        return apiService.searchMovies(Constants.API_KEY, query)
    }


    // ✅ פונקציה להחזרת apiService
    fun provideApiService(): TmdbApiService {
        return apiService
    }



}

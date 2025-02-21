package com.example.bingeme.domain.repositories

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
    override val apiService: TmdbApiService
) : TmdbApiBaseRepository(apiService) {

    suspend fun getPopularMovies(apiKey: String, page: Int): Response<MovieResponse> {
        return apiService.getPopularMovies(apiKey, page = page)
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

    suspend fun getTopRatedMovies(apiKey: String, page: Int = 1): Response<MovieResponse> {
        return apiService.getTopRatedMovies(apiKey, language = "en-US", page = page) // ✅ הוספת `language`
    }


    // ✅ פונקציה להחזרת apiService
    fun provideApiService(): TmdbApiService {
        return apiService
    }



}

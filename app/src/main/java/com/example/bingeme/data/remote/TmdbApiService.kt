package com.example.bingeme.data.remote

import com.example.bingeme.data.models.Movie
import com.example.bingeme.data.models.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Defines the API endpoints for interacting with the TMDB (The Movie Database) API.
 * This interface includes methods to fetch popular movies and retrieve details for a specific movie.
 */
interface TmdbApiService {

    /**
     * Fetches a list of popular movies from the TMDB API.
     *
     * @param apiKey The API key for authenticating the request.
     * @param language The language for the movie data (default is "en-US").
     * @param page The page number for paginated results (default is 1).
     * @return A Response object containing a MovieResponse with the list of popular movies.
     */
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Response<MovieResponse>

    /**
     * Fetches detailed information about a specific movie from the TMDB API.
     *
     * @param movieId The ID of the movie to retrieve details for.
     * @param apiKey The API key for authenticating the request.
     * @param language The language for the movie details (default is "en-US").
     * @return A Response object containing a Movie with the movie's details.
     */
    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US"
    ): Response<Movie>
}

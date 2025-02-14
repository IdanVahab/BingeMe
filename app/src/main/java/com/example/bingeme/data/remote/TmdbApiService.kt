package com.example.bingeme.data.remote

import com.example.bingeme.data.models.Movie
import com.example.bingeme.data.models.MovieResponse
import com.example.bingeme.data.models.Series
import com.example.bingeme.data.models.SeriesResponse
import com.example.bingeme.data.models.VideoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
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
     * Fetches a list of popular movies from the TMDB API.
     *
     * @param apiKey The API key for authenticating the request.
     * @param language The language for the movie data (default is "en-US").
     * @param page The page number for paginated results (default is 1).
     * @return A Response object containing a SeriesResponse with the list of popular tv series.
     */
    @GET("tv/popular")
    suspend fun getPopularTVSeries(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Response<SeriesResponse>

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

    /**
     * Fetches detailed information about a specific TV series from the TMDB API.
     *
     * @param tvId The ID of the TV series to retrieve details for.
     * @param apiKey The API key for authenticating the request.
     * @param language The language for the TV series details (default is "en-US").
     * @return A Response object containing a TVShow with the series' details.
     */
    @GET("tv/{tv_id}")
    suspend fun getTVSeriesDetails(
        @Path("tv_id") tvId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US"
    ): Response<Series>

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieTrailer(
        @Header("Authorization") token: String,
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = "en-US"
    ): Response<VideoResponse>

    @GET("tv/{tv_id}/videos")
    suspend fun getTVSeriesTrailer(
        @Header("Authorization") token: String,
        @Path("tv_id") tvId: Int,
        @Query("language") language: String = "en-US"
    ): Response<VideoResponse>

}

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


    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int = 1
    ): Response<MovieResponse>

    @GET("tv/popular")
    suspend fun getPopularTVSeries(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Response<SeriesResponse>



    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Response<Movie>

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


    @GET("search/movie")
    suspend fun searchMovies(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Response<MovieResponse>

    @GET("search/tv")
    suspend fun searchSeries(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Response<SeriesResponse>

}

package com.example.bingeme.data.models

import com.google.gson.annotations.SerializedName

/**
 * Data class representing a movie model in the application.
 * This class is used for managing movie data, displaying movie details in the UI,
 * and mapping API responses from TMDB.
 *
 * @param id Unique identifier for the movie.
 * @param title The title of the movie.
 * @param overview A brief description of the movie, nullable.
 * @param posterPath URL path to the movie's poster image, nullable.
 * @param releaseDate The release date of the movie, nullable.
 * @param popularity The popularity score of the movie.
 * @param voteAverage The average rating score of the movie.
 * @param adult Indicates whether the movie is intended for adults.
 * @param originalLanguage The original language of the movie.
 * @param genres A list of genres associated with the movie.
 * @param isFavorite Indicates whether the movie is marked as a favorite by the user.
 * @param isWatched Indicates whether the movie has been watched by the user.
 * @param trailerUrl The URL to the movie's trailer (processed separately).
 */
data class Movie(
    override val id: Int,
    override val title: String,
    override val overview: String?,
    @SerializedName("poster_path")
    override val posterPath: String?,
    @SerializedName("release_date")
    override val releaseDate: String?,
    override val popularity: Double,
    @SerializedName("vote_average")
    override val voteAverage: Double,
    @SerializedName("adult")
    override val adult: Boolean,
    @SerializedName("original_language")
    override val originalLanguage: String,
    @SerializedName("genres")
    override val genres: List<Genre>,
    @SerializedName("trailer_url")
    override val trailerUrl: String?,
    override var isFavorite: Boolean = false,
    override var isWatched: Boolean = false,
    @SerializedName("runtime")
    val runtime: Int?
) : BaseMediaItem()

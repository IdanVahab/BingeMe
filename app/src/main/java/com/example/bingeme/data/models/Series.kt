package com.example.bingeme.data.models

import com.google.gson.annotations.SerializedName

/**
 * Data class representing a TV series model in the application.
 * This class is used for managing series data, displaying series details in the UI,
 * and mapping API responses from TMDB.
 *
 * @param id Unique identifier for the series.
 * @param title The title of the series.
 * @param overview A brief description of the series, nullable.
 * @param posterPath URL path to the series' poster image, nullable.
 * @param releaseDate The date when the series first aired, nullable.
 * @param popularity The popularity score of the series.
 * @param voteAverage The average rating score of the series.
 * @param adult Indicates whether the series is intended for adults.
 * @param originalLanguage The original language of the series.
 * @param genres A list of genres associated with the series.
 * @param isFavorite Indicates whether the series is marked as a favorite by the user.
 * @param isWatched Indicates whether the series has been watched by the user.
 * @param numberOfEpisodes The total number of episodes in the series.
 * @param numberOfSeasons The total number of seasons in the series.
 */
data class Series(
    override val id: Int,
    @SerializedName("name")
    override val title: String,
    override val overview: String?,
    @SerializedName("poster_path")
    override val posterPath: String?,
    @SerializedName("first_air_date")
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
    @SerializedName("number_of_episodes")
    val numberOfEpisodes: Int,
    @SerializedName("number_of_seasons")
    val numberOfSeasons: Int
) : BaseMediaItem()

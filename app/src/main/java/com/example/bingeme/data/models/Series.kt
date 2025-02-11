package com.example.bingeme.data.models

import com.google.gson.annotations.SerializedName

/**
 * Represents a series model in the application.
 * This data class is used for managing series data, such as displaying information in the UI
 * or mapping API responses.
 *
 * @param id Unique identifier for the series.
 * @param title The title of the series, nullable in case the data is unavailable.
 * @param overview A brief description of the series, nullable.
 * @param poster_path URL path to the series' poster image, nullable.
 * @param releaseDate The date when the series first aired, nullable.
 * @param isFavorite Indicates whether the series is marked as a favorite by the user.
 * @param popularity The popularity score of the series.
 * @param number_of_episodes The total number of episodes in the series.
 * @param number_of_seasons The total number of seasons in the series.
 */
data class Series(
    override val id: Int,                 // Unique series ID
    @SerializedName("name")
    override val title: String,           // Series title (nullable)
    override val overview: String?,       // Short description (nullable)
    @SerializedName("poster_path")
    override val posterPath:String?, // Path to poster image (nullable)
    @SerializedName("first_air_date")
    override val releaseDate: String?, // First air date (nullable)
    override var isFavorite: Boolean,    // Flag for favorite status
    val popularity: Double,      // Popularity score
    val number_of_episodes: Int, // Total number of episodes in the series
    val number_of_seasons: Int   // Total number of seasons in the series
) : BaseMediaItem()

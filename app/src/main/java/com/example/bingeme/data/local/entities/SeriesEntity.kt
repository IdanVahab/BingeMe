package com.example.bingeme.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a series entity stored in the local Room database.
 * This data class defines the schema for the "series" table.
 *
 * @param id Unique identifier for the series.
 * @param title The title of the series.
 * @param overview A brief description of the series.
 * @param posterPath URL path to the series' poster image.
 * @param releaseDate The date when the series first aired.
 * @param popularity The series' popularity score.
 * @param voteAverage The average rating score of the series.
 * @param adult Indicates whether the series is intended for adults.
 * @param originalLanguage The original language of the series.
 * @param isFavorite Indicates whether the series is marked as a favorite by the user.
 * @param isWatched Indicates whether the series has been watched by the user.
 * @param numberOfEpisodes The total number of episodes in the series.
 * @param numberOfSeasons The total number of seasons in the series.
 * @param trailerUrl The URL to the series' trailer.
 */
@Entity(tableName = "series")
data class SeriesEntity(
    @PrimaryKey val id: Int,            // Unique series ID
    val title: String,                  // Series title (was "name", now standardized)
    val overview: String,               // Short description of the series
    val posterPath: String,             // Path to poster image
    val releaseDate: String,            // First air date of the series
    val popularity: Double,              // Popularity score of the series
    val voteAverage: Double,             // Average rating score
    val adult: Boolean,                  // Indicates if the series is for adults
    val originalLanguage: String,        // Original language of the series
    val isFavorite: Boolean = false,     // Flag to mark the series as favorite
    val isWatched: Boolean = false,      // Indicates if the series has been watched
    val numberOfEpisodes: Int,           // Total number of episodes in the series
    val numberOfSeasons: Int,            // Total number of seasons in the series
    val trailerUrl: String?              // URL to the series' trailer
)

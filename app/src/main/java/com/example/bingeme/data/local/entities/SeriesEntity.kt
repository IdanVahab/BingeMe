package com.example.bingeme.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a series entity stored in the local Room database.
 * This data class defines the schema for the "series" table.
 *
 * @param id Unique identifier for the series.
 * @param name The title of the series.
 * @param overview A brief description of the series.
 * @param posterPath URL path to the series' poster image.
 * @param releaseDate The date when the series first aired.
 * @param isavorite Indicates whether the series is marked as a favorite by the user.
 * @param popularity The series' popularity score, represented as a Double.
 * @param number_of_episodes The total number of episodes in the series.
 * @param number_of_seasons The total number of seasons in the series.
 */
@Entity(tableName = "series")
data class SeriesEntity(
    @PrimaryKey val id: Int,            // Unique series ID
    val name: String,                   // Series title
    val overview: String,                // Short description of the series
    val posterPath: String,             // Path to poster image
    val releaseDate: String,          // First air date of the series
    val isFavorite: Boolean,            // Flag to mark the series as favorite
    val popularity: Double,              // Popularity score of the series
    val number_of_episodes: Int,         // Total number of episodes in the series
    val number_of_seasons: Int           // Total number of seasons in the series
)

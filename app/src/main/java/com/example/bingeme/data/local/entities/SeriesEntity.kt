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
 * @param firstAirDate The date when the series first aired.
 */
@Entity(tableName = "series")
data class SeriesEntity(
    @PrimaryKey val id: Int,           // Unique series ID
    val title: String,                 // Series title
    val overview: String,              // Short description of the series
    val posterPath: String,            // Path to poster image
    val firstAirDate: String           // First air date of the series
)

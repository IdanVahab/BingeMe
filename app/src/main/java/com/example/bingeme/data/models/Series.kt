package com.example.bingeme.data.models

/**
 * Represents a series in the application.
 * This class can be expanded to include properties or methods for handling
 * series-related data, such as displaying series details or parsing API responses.
 *
 * Currently, this class is empty but can be utilized for API responses or data manipulation.
 */
class Series (
    val id: Int,                 // Unique Series ID
    val name: String?,          // Series title (nullable)
    val overview: String?,       // Short description (nullable)
    val poster_path: String?,    // Path to poster image (nullable)
    val first_air_date: String?,    // Release date (nullable)
    var isFavorite: Boolean,     // Flag for favorite status
    val popularity: Double,     // Average rating of the Series
    val numberOfEpisodes:Int,       // Total number of episodes in the series
    val numberOfSeasons:Int     // Total number of seasons in the series

    )

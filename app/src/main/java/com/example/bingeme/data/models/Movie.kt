package com.example.bingeme.data.models

/**
 * Represents a movie model in the application.
 * This data class is used for managing movie data, such as displaying information in the UI
 * or mapping API responses.
 *
 * @param id Unique identifier for the movie.
 * @param title The title of the movie, nullable in case the data is unavailable.
 * @param overview A brief description of the movie, nullable.
 * @param poster_path URL path to the movie's poster image, nullable.
 * @param releaseDate The release date of the movie, nullable.
 * @param isFavorite Indicates whether the movie is marked as a favorite by the user.
 * @param rating The average rating of the movie, represented as a Double.
 */
data class Movie(
    val id: Int,                 // Unique movie ID
    val title: String?,          // Movie title (nullable)
    val overview: String?,       // Short description (nullable)
    val poster_path: String?,    // Path to poster image (nullable)
    val releaseDate: String?,    // Release date (nullable)
    var isFavorite: Boolean,     // Flag for favorite status
    val rating: Double           // Average rating of the movie
)

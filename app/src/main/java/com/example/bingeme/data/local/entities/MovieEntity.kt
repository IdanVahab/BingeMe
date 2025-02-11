package com.example.bingeme.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a movie entity stored in the local Room database.
 * This data class defines the schema for the "movies" table.
 *
 * @param id Unique identifier for the movie.
 * @param title The title of the movie.
 * @param overview A brief description of the movie.
 * @param poster_path URL path to the movie's poster image.
 * @param releaseDate The release date of the movie.
 * @param isFavorite Indicates whether the movie is marked as a favorite by the user.
 * @param rating The movie's average rating, represented as a Double.
 */
@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: Int,            // Unique movie ID
    val title: String,                  // Movie title
    val overview: String,               // Short description of the movie
    val poster_path: String,            // Path to poster image
    val release_date: String,            // Release date in string format
    val isFavorite: Boolean,            // Flag to mark the movie as favorite
    val rating: Double                  // Average rating of the movie
)

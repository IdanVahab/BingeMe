package com.example.bingeme.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.bingeme.data.models.Genre

/**
 * Represents a movie entity stored in the local Room database.
 * This data class defines the schema for the "movies" table.
 *
 * @param id Unique identifier for the movie.
 * @param title The title of the movie.
 * @param overview A brief description of the movie.
 * @param posterPath URL path to the movie's poster image.
 * @param releaseDate The release date of the movie.
 * @param popularity The popularity score of the movie.
 * @param voteAverage The average rating score of the movie.
 * @param adult Indicates whether the movie is intended for adults.
 * @param originalLanguage The original language of the movie.
 * @param isFavorite Indicates whether the movie is marked as a favorite by the user.
 * @param isWatched Indicates whether the movie has been watched by the user.
 * @param trailerUrl The URL to the movie's trailer.
 * @param runtime The runtime of the movie in minutes (nullable).
 */
@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: Int,            // Unique movie ID
    val title: String,                  // Movie title
    val overview: String,               // Short description of the movie
    val posterPath: String,             // Path to poster image
    val releaseDate: String,            // Release date in string format
    val popularity: Double,             // Popularity score of the movie
    val voteAverage: Double,            // Average rating score
    val adult: Boolean,                 // Indicates if the movie is for adults
    val originalLanguage: String,       // Original language of the movie
    val isFavorite: Boolean = false,    // Flag to mark the movie as favorite
    val isWatched: Boolean = false,     // Indicates if the movie has been watched
    val trailerUrl: String?,            // URL to the movie's trailer
    val runtime: Int?,                    // Runtime of the movie in minutes (nullable)
    val genres: List<Genre>

    )

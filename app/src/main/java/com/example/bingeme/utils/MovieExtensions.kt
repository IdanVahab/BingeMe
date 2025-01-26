package com.example.bingeme.utils

import com.example.bingeme.data.local.entities.MovieEntity
import com.example.bingeme.data.models.Movie

/**
 * Extension functions for converting between Movie and MovieEntity.
 */

/**
 * Converts a Movie object to a MovieEntity object.
 *
 * @return The corresponding MovieEntity.
 */
fun Movie.toEntity(): MovieEntity {
    return MovieEntity(
        id = id,
        title = title ?: "",
        overview = overview ?: "",
        poster_path = poster_path ?: "",
        releaseDate = releaseDate ?: "",
        isFavorite = isFavorite,
        rating = rating
    )
}

/**
 * Converts a MovieEntity object to a Movie object.
 *
 * @return The corresponding Movie.
 */
fun MovieEntity.toModel(): Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview,
        poster_path = poster_path,
        releaseDate = releaseDate,
        isFavorite = isFavorite,
        rating = rating
    )
}

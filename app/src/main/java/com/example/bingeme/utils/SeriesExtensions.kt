package com.example.bingeme.utils

import com.example.bingeme.data.local.entities.SeriesEntity
import com.example.bingeme.data.models.Series

/**
 * Converts a Series object to a SeriesEntity object.
 *
 * @return The corresponding SeriesEntity.
 */
fun Series.toEntity(): SeriesEntity {
    return SeriesEntity(
        id = id,
        name = title ?: "",
        overview = overview ?: "",
        posterPath = posterPath ?: "",
        releaseDate = releaseDate ?: "",
        isFavorite = isFavorite,
        popularity = popularity,
        number_of_episodes = number_of_episodes,
        number_of_seasons = number_of_seasons,
    )
}

/**
 * Converts a SeriesEntity object to a Series object.
 *
 * @return The corresponding Series.
 */
fun SeriesEntity.toModel(): Series {
    return Series(
        id = id,
        title = name,
        overview = overview,
        posterPath = posterPath,
        releaseDate = releaseDate,
        isFavorite = isFavorite,
        popularity = popularity,
        number_of_episodes = number_of_episodes,
        number_of_seasons = number_of_seasons,
    )
}

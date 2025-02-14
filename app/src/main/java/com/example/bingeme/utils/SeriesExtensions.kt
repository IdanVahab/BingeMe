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
        title = title ?: "",
        overview = overview ?: "",
        posterPath = posterPath ?: "",
        releaseDate = releaseDate ?: "",
        popularity = popularity,
        voteAverage = voteAverage,
        adult = adult,
        originalLanguage = originalLanguage,
        isFavorite = isFavorite,
        isWatched = isWatched,
        numberOfEpisodes = numberOfEpisodes,
        numberOfSeasons = numberOfSeasons,
        trailerUrl = trailerUrl
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
        title = title,
        overview = overview,
        posterPath = posterPath,
        releaseDate = releaseDate,
        popularity = popularity,
        voteAverage = voteAverage,
        adult = adult,
        originalLanguage = originalLanguage,
        isFavorite = isFavorite,
        isWatched = isWatched,
        numberOfEpisodes = numberOfEpisodes,
        numberOfSeasons = numberOfSeasons,
        trailerUrl = trailerUrl,
        genres = emptyList() // Since genres are not stored in SeriesEntity, returning an empty list
    )
}

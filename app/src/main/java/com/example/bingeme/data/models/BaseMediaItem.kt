package com.example.bingeme.data.models

/**
 * Abstract base class for media items (movies and series).
 * This class defines the common properties shared by both movies and TV series.
 *
 * @param id Unique identifier for the media item.
 * @param title The title of the media item.
 * @param posterPath URL path to the media's poster image, nullable.
 * @param overview A brief description of the media, nullable.
 * @param releaseDate The release date of the movie or first air date of the series, nullable.
 * @param popularity The popularity score of the media item.
 * @param voteAverage The average rating score of the media item.
 * @param adult Indicates whether the media item is intended for adults.
 * @param originalLanguage The original language of the media item.
 * @param genres A list of genres associated with the media item.
 * @param isFavorite Indicates whether the media item is marked as a favorite by the user.
 * @param isWatched Indicates whether the media item has been watched by the user.
 * @param trailerUrl The URL to the media's trailer (processed separately).
 */
abstract class BaseMediaItem{
    abstract val id: Int
    abstract val title: String
    abstract val posterPath: String?
    abstract val overview: String?
    abstract val releaseDate: String?
    abstract val popularity: Double
    abstract val voteAverage: Double
    abstract val adult: Boolean
    abstract val originalLanguage: String
    abstract val genres: List<Genre>
    abstract val trailerUrl: String?
    abstract var isFavorite: Boolean
    abstract var isWatched: Boolean

}

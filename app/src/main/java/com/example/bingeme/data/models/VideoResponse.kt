package com.example.bingeme.data.models
import com.google.gson.annotations.SerializedName

/**
 * Represents a YouTube trailer key retrieved from the TMDB API.
 *
 * @param key The unique identifier for the YouTube video.
 * @param site The platform where the video is hosted (e.g., "YouTube").
 */
data class YoutubeTrailerKey(
    val key: String,
    val site: String
)

/**
 * Represents the response from the TMDB API when requesting video details.
 *
 * @param results A list of videos, including trailers and teasers.
 */
data class VideoResponse(
    @SerializedName("results")
    val results: List<YoutubeTrailerKey>
)

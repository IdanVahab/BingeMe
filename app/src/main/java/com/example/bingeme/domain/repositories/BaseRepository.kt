package com.example.bingeme.domain.repositories

import com.example.bingeme.data.remote.TmdbApiService
import com.example.bingeme.data.models.VideoResponse
import retrofit2.Response
import javax.inject.Inject

/**
 * Base repository class for handling common operations across repositories.
 * This class manages API interactions and provides helper functions such as fetching trailer URLs.
 *
 * @param apiService Injected TMDB API service for fetching movie and series data.
 */
open class BaseRepository @Inject constructor(
    protected val apiService: TmdbApiService
) {

    /**
     * Fetches the YouTube trailer URL for a given movie or TV series.
     *
     * @param token The Bearer Token for authentication.
     * @param id The ID of the movie or TV series.
     * @param isMovie Boolean flag indicating if the ID is for a movie (true) or TV series (false).
     * @return The YouTube trailer URL, or null if no trailer is found.
     */
    suspend fun getTrailerUrl(token: String, id: Int, isMovie: Boolean): String? {
        val response: Response<VideoResponse> = if (isMovie) {
            apiService.getMovieTrailer(token, id)
        } else {
            apiService.getTVSeriesTrailer(token, id)
        }

        if (response.isSuccessful) {
            val videos = response.body()?.results ?: emptyList()
            val trailer = videos.firstOrNull { it.site == "YouTube" && it.key.isNotEmpty() }
            return trailer?.let { "https://www.youtube.com/watch?v=${it.key}" }
        }
        return null
    }
}

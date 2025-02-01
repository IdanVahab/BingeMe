package com.example.bingeme.domain.repositories

import com.example.bingeme.data.local.dao.WatchlistDao
import com.example.bingeme.data.remote.TmdbApiService
import javax.inject.Inject

/**
 * Repository class for managing series-related data.
 * This class interacts with the TMDB API for fetching series data.
 *
 * @param apiService Injected TMDB API service for fetching series data.
 */
class SeriesRepository @Inject constructor(
    private val apiService: TmdbApiService,
    private val watchlistDao: WatchlistDao
) {

    /**
     * Fetches a list of popular series from the TMDB API.
     *
     * @param apiKey The API key for authentication.
     * @return A response containing the popular series.
     */
    suspend fun getPopularSeries(apiKey: String) = apiService.getPopularTVSeries(apiKey) // Note: Adjust endpoint if necessary.

    suspend fun getMovieDetails(apiKey: String, seriesId: Int) =
        apiService.getTVSeriesDetails(seriesId, apiKey)
}

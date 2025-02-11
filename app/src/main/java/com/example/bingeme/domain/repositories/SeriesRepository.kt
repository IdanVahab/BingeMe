package com.example.bingeme.domain.repositories

import com.example.bingeme.data.local.dao.WatchlistDao
import com.example.bingeme.data.local.entities.SeriesEntity
import com.example.bingeme.data.models.Series
import com.example.bingeme.data.remote.TmdbApiService
import com.example.bingeme.utils.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Repository class for managing series-related data.
 * This class interacts with the TMDB API for fetching series data.
 *
 * @param apiService Injected TMDB API service for fetching series data.
 * @param watchlistDao Injected DAO for accessing watchlist-related database operations.
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
    suspend fun getPopularSeries(apiKey: String) = apiService.getPopularTVSeries(apiKey)

    /**
     * Fetches details of a specific series from the TMDB API.
     *
     * @param apiKey The API key for authentication.
     * @param seriesId The ID of the series to fetch details for.
     * @return A response containing the series details.
     */
    suspend fun getSeriesDetails(apiKey: String, seriesId: Int) =
        apiService.getTVSeriesDetails(seriesId, apiKey)

    /**
     * Adds a series to the watchlist if it is not already present.
     *
     * @param series The series entity to add to the watchlist.
     */
    suspend fun addSeriesToWatchlist(series: SeriesEntity) {
        if (!watchlistDao.isSeriesInWatchlist(series.id)) {
            watchlistDao.insertSeries(series)
        }
    }

    /**
     * Removes a series from the watchlist.
     *
     * @param series The series entity to remove from the watchlist.
     */
    suspend fun removeSeriesFromWatchlist(series: SeriesEntity) {
        watchlistDao.deleteSeries(series)
    }

    /**
     * Retrieves all series from the watchlist.
     * The result is provided as a Flow for observing changes in real-time.
     *
     * @return A Flow emitting a list of series in the watchlist.
     */
    fun getAllSeriesFromWatchlist(): Flow<List<Series>> {
        return watchlistDao.getAllSeries().map { entities ->
            entities.map { it.toModel() }
        }
    }
}

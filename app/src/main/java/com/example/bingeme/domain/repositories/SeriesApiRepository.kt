package com.example.bingeme.domain.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.bingeme.data.models.Series
import com.example.bingeme.data.models.SeriesResponse
import com.example.bingeme.data.paging.SeriesPagingSource
import com.example.bingeme.data.remote.TmdbApiService
import com.example.bingeme.utils.Constants
import retrofit2.Response
import javax.inject.Inject

/**
 * Repository class for managing series-related data.
 * This class interacts with the TMDB API for fetching series data and extends BaseRepository
 * to inherit common functionality, including fetching trailers.
 *
 * @param apiService Injected TMDB API service for fetching series data.
 * @param mediaDao Injected DAO for accessing watchlist-related database operations.
 */
class SeriesApiRepository @Inject constructor(
    apiService: TmdbApiService
) : TmdbApiBaseRepository(apiService) {  // Extend BaseRepository

    /**
     * Fetches a list of popular series from the TMDB API.
     *
     * @param token The Bearer Token for authentication.
     * @return A response containing the popular series.
     */

    suspend fun getPopularSeries(page: Int): Response<SeriesResponse> {
        return apiService.getPopularTVSeries(
            apiKey = Constants.API_KEY,
            language = "en-US", // ✅ חובה לציין את הפרמטר
            page = page // ✅ מספקים את מספר העמוד
        )
    }

    /**
     * Fetches details of a specific series from the TMDB API.
     *
     * @param token The Bearer Token for authentication.
     * @param seriesId The ID of the series to fetch details for.
     * @return A response containing the series details.
     */
    suspend fun getSeriesDetails(apiKey: String, token: String, seriesId: Int): Response<Series> {
        val response: Response<Series> = apiService.getTVSeriesDetails(seriesId, apiKey)

        if (response.isSuccessful) {
            response.body()?.let { series ->
                // Fetch the trailer URL
                val trailerUrl = getTrailerUrl(token, seriesId, isMovie = false)

                // Update the series object with the trailer URL
                val updatedSeries = series.copy(trailerUrl = trailerUrl)

                // Return the updated Response<Series>
                return Response.success(updatedSeries)
            }
        }
        return response // Return the original response if unsuccessful
    }



    suspend fun searchSeries(query: String): Response<SeriesResponse> {
        return apiService.searchSeries(Constants.API_KEY, query)
    }

    fun getMostPopularSeries(): Pager<Int, Series> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { SeriesPagingSource(apiService, Constants.API_KEY) }
        )
    }




}

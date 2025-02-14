package com.example.bingeme.presentation.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bingeme.data.models.Series
import com.example.bingeme.domain.repositories.SeriesRepository
import com.example.bingeme.domain.repositories.WatchlistRepository
import com.example.bingeme.utils.Constants
import com.example.bingeme.utils.toEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the SeriesDetailsFragment, responsible for handling business logic
 * and interacting with repositories to fetch series details and manage favorites.
 *
 * @param seriesRepository Repository for managing series-related operations.
 * @param repository Repository for managing watchlist-related operations.
 */
@HiltViewModel
class SeriesDetailsFragmentViewModel @Inject constructor(
    private val seriesRepository: SeriesRepository,
    private val repository: WatchlistRepository
) : ViewModel() {


    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> get() = _isFavorite


    fun checkIfFavorite(seriesId: Int) {
        viewModelScope.launch {
            val isInFavorites = repository.isSeriesInWatchlist(seriesId)

            _isFavorite.postValue(isInFavorites)
        }
    }

    /**
     * Fetches details of a specific series by its ID.
     *
     * @param seriesId The ID of the series.
     * @return A Flow emitting the Result of the operation with the Series details.
     */
    fun getSeriesDetails(seriesId: Int): Flow<Result<Series?>> = flow {
        try {
            val response = seriesRepository.getSeriesDetails(Constants.API_KEY,Constants.TOKEN,seriesId)
            if (response.isSuccessful) {
                emit(Result.success(response.body()))
            } else {
                emit(Result.failure(Exception("Error: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Toggles the favorite status of a series in the watchlist.
     *
     * @param series The series to toggle.
     */
    fun toggleFavorite(series: Series) {
        viewModelScope.launch {
            val seriesEntity = series.toEntity()
            if (_isFavorite.value == true) {
                repository.removeSeries(seriesEntity)
                _isFavorite.value = false
            } else {
                repository.addSeries(seriesEntity)
                _isFavorite.value = true
            }
        }
    }
}

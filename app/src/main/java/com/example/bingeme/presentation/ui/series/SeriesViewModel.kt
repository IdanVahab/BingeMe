package com.example.bingeme.presentation.ui.series

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bingeme.data.models.Series
import com.example.bingeme.domain.repositories.SeriesRepository
import com.example.bingeme.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the SeriesFragment, responsible for managing the data and business logic
 * for displaying popular TV series.
 *
 * @param seriesRepository Repository for fetching series data from the API.
 */
@HiltViewModel
class SeriesViewModel @Inject constructor(
    private val seriesRepository: SeriesRepository
) : ViewModel() {

    private val _popularSeries = MutableStateFlow<List<Series>>(emptyList())
    val popularSeries: StateFlow<List<Series>> get() = _popularSeries

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    init {
        fetchPopularSeries()
    }

    /**
     * Fetches a list of popular TV series from the API and updates the StateFlow.
     */
    private fun fetchPopularSeries() {
        viewModelScope.launch {
            try {
                val response = seriesRepository.getPopularSeries(Constants.API_KEY)
                if (response.isSuccessful) {
                    _popularSeries.value = response.body()?.results ?: emptyList()
                    _errorMessage.value = null
                } else {
                    _popularSeries.value = emptyList()
                    _errorMessage.value = "Failed to fetch series: ${response.message()}"
                }
            } catch (e: Exception) {
                _popularSeries.value = emptyList()
                _errorMessage.value = "Error: ${e.localizedMessage}"
            }
        }
    }
}

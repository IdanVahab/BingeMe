package com.example.bingeme.presentation.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bingeme.data.models.Movie
import com.example.bingeme.domain.repositories.MoviesRepository
import com.example.bingeme.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for MainFragment, responsible for managing the data and business logic
 * for displaying popular movies.
 *
 * @param moviesRepository Repository for fetching movie data from the API.
 */
@HiltViewModel
class MainFragmentViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    private val _popularMovies = MutableStateFlow<List<Movie>>(emptyList())
    val popularMovies: StateFlow<List<Movie>> get() = _popularMovies

    init {
        fetchPopularMovies()
    }

    /**
     * Fetches a list of popular movies from the API and updates the StateFlow.
     */
    private fun fetchPopularMovies() {
        viewModelScope.launch {
            try {
                val response = moviesRepository.getPopularMovies(Constants.API_KEY)
                if (response.isSuccessful) {
                    _popularMovies.value = response.body()?.results ?: emptyList()
                } else {
                    _popularMovies.value = emptyList() // Handle API error
                }
            } catch (e: Exception) {
                _popularMovies.value = emptyList() // Handle general errors
            }
        }
    }
}

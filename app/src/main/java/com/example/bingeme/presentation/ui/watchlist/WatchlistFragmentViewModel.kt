package com.example.bingeme.presentation.ui.watchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bingeme.data.models.Movie
import com.example.bingeme.data.models.Series
import com.example.bingeme.domain.repositories.WatchlistRepository
import com.example.bingeme.utils.toEntity
import com.example.bingeme.utils.toModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the WatchlistFragment, responsible for managing the watchlist data
 * and providing it to the UI.
 *
 * @param repository Repository for accessing watchlist-related operations.
 */
@HiltViewModel
class WatchlistFragmentViewModel @Inject constructor(
    private val repository: WatchlistRepository
) : ViewModel() {

    /**
     * Flow emitting the list of movies in the watchlist.
     */
    val watchlistMovies: Flow<List<Movie>> = repository.getAllMovies().map { entities ->
        entities.map { it.toModel() }
    }

    val watchlistSeries: Flow<List<Series>> = repository.getAllSeries().map { entities ->
        entities.map { it.toModel() }
    }

    /**
     * Removes a movie from the watchlist.
     *
     * @param movie The movie to remove.
     */
    fun removeMovieFromWatchlist(movie: Movie) {
        viewModelScope.launch {
            repository.removeMovie(movie.toEntity())
        }
    }

    fun removeSeriesFromWatchlist(series: Series) {
        viewModelScope.launch {
            repository.removeSeries(series.toEntity())
        }
    }
}

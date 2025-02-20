package com.example.bingeme.presentation.ui.watched

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bingeme.data.models.Movie
import com.example.bingeme.data.models.Series
import com.example.bingeme.domain.repositories.WatchedRepository
import com.example.bingeme.utils.toEntity
import com.example.bingeme.utils.toModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchedFragmentViewModel @Inject constructor(
    private val repository: WatchedRepository
) : ViewModel() {

    val watchedMovies: Flow<List<Movie>> = repository.getAllWatchedMovies().map { entities ->
        entities.map { it.toModel() }
    }

    val watchedSeries: Flow<List<Series>> = repository.getAllWatchedSeries().map { entities ->
        entities.map { it.toModel() }
    }

    fun removeMovieFromWatched(movie: Movie) {
        viewModelScope.launch {
            repository.removeMovie(movie.toEntity())
        }
    }

    fun removeSeriesFromWatched(series: Series) {
        viewModelScope.launch {
            repository.removeSeries(series.toEntity())
        }
    }
}

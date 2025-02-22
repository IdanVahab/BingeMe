package com.example.bingeme.presentation.ui.watched

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bingeme.data.models.Movie
import com.example.bingeme.data.models.Series
import com.example.bingeme.domain.repositories.MediaDBRepository
import com.example.bingeme.domain.repositories.WatchedRepository
import com.example.bingeme.presentation.ui.main.MainFragmentViewModel.ListType
import com.example.bingeme.utils.toEntity
import com.example.bingeme.utils.toModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchedFragmentViewModel @Inject constructor(
    private val mediaDBRepository: MediaDBRepository
) : ViewModel() {

    val watchedMovies: Flow<List<Movie>> = mediaDBRepository.getWatchedMovies()

    val watchedSeries: Flow<List<Series>> = mediaDBRepository.getWatchedSeries()


    private val _currentListType = MutableStateFlow(ListType.MOVIES)
    val currentListType: StateFlow<ListType> get() = _currentListType

    fun modifyMovie(movie: Movie){
        viewModelScope.launch {
            val movieEntity = movie.toEntity()
            mediaDBRepository.addMovie(movieEntity)
        }
    }

    fun modifySeries(series: Series){
        viewModelScope.launch {
            val seriesEntity = series.toEntity()
            mediaDBRepository.addSeries(seriesEntity)
        }
    }

    // ✅ עדכון סוג הרשימה הפעילה
    fun setCurrentListType(type: ListType) {
        _currentListType.value = type
    }




    fun getWatchedMovies(){

    }
}

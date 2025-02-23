package com.example.bingeme.presentation.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bingeme.data.models.Movie
import com.example.bingeme.data.models.Series
import com.example.bingeme.domain.repositories.MediaDBRepository
import com.example.bingeme.presentation.ui.main.MainFragmentViewModel.ListType
import com.example.bingeme.utils.toEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteFragmentViewModel @Inject constructor(
    private val mediaDBRepository: MediaDBRepository
) : ViewModel() {

    val favoriteMovies: Flow<List<Movie>> = mediaDBRepository.getFavoriteMovies()

    val favoriteSeries: Flow<List<Series>> = mediaDBRepository.getFavoriteSeries()


    private val _currentListType = MutableStateFlow(ListType.MOVIES)
    val currentListType: StateFlow<ListType> get() = _currentListType

    fun modifyMovie(movie: Movie){
        viewModelScope.launch {
            val movieEntity = movie.toEntity()
            if(movieEntity.isFavorite||movieEntity.isWatched) {
                mediaDBRepository.addEditMovie(movieEntity)
            }else{
                mediaDBRepository.removeMovie(movieEntity)
            }
        }
    }

    fun modifySeries(series: Series){
        viewModelScope.launch {
            val seriesEntity = series.toEntity()
            if(seriesEntity.isFavorite||seriesEntity.isWatched) {
                mediaDBRepository.addEditSeries(seriesEntity)
            }else{
                mediaDBRepository.removeSeries(seriesEntity)
            }

        }
    }

    // ✅ עדכון סוג הרשימה הפעילה
    fun setCurrentListType(type: ListType) {
        _currentListType.value = type
    }

    fun getFavoriteMovies(){

    }
}

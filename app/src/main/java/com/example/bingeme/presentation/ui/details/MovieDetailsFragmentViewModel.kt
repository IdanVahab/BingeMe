package com.example.bingeme.presentation.ui.details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bingeme.data.models.Movie
import com.example.bingeme.domain.repositories.MoviesRepository
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
 * ViewModel for the MovieDetailsFragment, responsible for handling business logic
 * and interacting with repositories to fetch movie details and manage favorites.
 *
 * @param moviesRepository Repository for managing movie-related operations.
 * @param repository Repository for managing watchlist-related operations.
 */
@HiltViewModel
class MovieDetailsFragmentViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val repository: WatchlistRepository
) : ViewModel() {

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> get() = _isFavorite

    fun checkIfFavorite(movieId: Int) {
        viewModelScope.launch {
            val isInFavorites = repository.isMovieInFavoritelist(movieId)
            if (_isFavorite.value != isInFavorites) { // ✅ עדכן רק אם יש שינוי אמיתי
                Log.d("MovieDetailsViewModel", "Updating LiveData: isFavorite = $isInFavorites")
                _isFavorite.postValue(isInFavorites)
            }
        }
    }

    /**
     * Fetches details of a specific movie by its ID.
     *
     * @param movieId The ID of the movie.
     * @return A Flow emitting the Result of the operation with the Movie details.
     */
    fun getMovieDetails(movieId: Int): Flow<Result<Movie?>> = flow {
        try {
            val response = moviesRepository.getMovieDetails(Constants.API_KEY,Constants.TOKEN, movieId)
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
     * Toggles the favorite status of a movie in the watchlist.
     *
     * @param movie The movie to toggle.
     */
    fun toggleFavorite(movie: Movie) {
        viewModelScope.launch {
            val movieEntity = movie.toEntity()
            if (_isFavorite.value == true) {
                repository.removeMovie(movieEntity)
                _isFavorite.value = false
            } else {
                repository.addMovie(movieEntity)
                _isFavorite.value = true
            }
        }
    }
    fun toggleWatched(movieId: Int) {
        viewModelScope.launch {
            val isWatched = moviesRepository.isMovieWatched(movieId)
            if (isWatched) {
                moviesRepository.unmarkMovieAsWatched(movieId)
            } else {
                moviesRepository.markMovieAsWatched(movieId)
            }
        }
    }


}

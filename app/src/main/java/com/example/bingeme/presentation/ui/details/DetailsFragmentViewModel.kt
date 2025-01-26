package com.example.bingeme.presentation.ui.details

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
 * ViewModel for the DetailsFragment, responsible for handling business logic
 * and interacting with repositories to fetch movie details and manage favorites.
 *
 * @param moviesRepository Repository for managing movie-related operations.
 * @param repository Repository for managing watchlist-related operations.
 */
@HiltViewModel
class DetailsFragmentViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val repository: WatchlistRepository
) : ViewModel() {

    /**
     * Fetches details of a specific movie by its ID.
     *
     * @param movieId The ID of the movie.
     * @return A Flow emitting the Result of the operation with the Movie details.
     */
    fun getMovieDetails(movieId: Int): Flow<Result<Movie?>> = flow {
        try {
            val response = moviesRepository.getMovieDetails(Constants.API_KEY, movieId)
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
            if (repository.isMovieInWatchlist(movie.id)) {
                repository.removeMovie(movieEntity)
                movie.isFavorite = false
            } else {
                repository.addMovie(movieEntity)
                movie.isFavorite = true
            }
        }
    }
}

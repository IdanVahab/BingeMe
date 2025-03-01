package com.example.bingeme.presentation.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.bingeme.data.models.Movie
import com.example.bingeme.data.models.Series
import com.example.bingeme.data.paging.MoviesPagingSource
import com.example.bingeme.domain.repositories.MediaDBRepository
import com.example.bingeme.domain.repositories.MoviesApiRepository
import com.example.bingeme.domain.repositories.SeriesApiRepository
import com.example.bingeme.utils.Constants
import com.example.bingeme.utils.toEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject constructor(
    private val moviesRepository: MoviesApiRepository,
    private val seriesRepository: SeriesApiRepository,
    private val mediaDBRepository: MediaDBRepository


) : ViewModel() {

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> get() = _isFavorite

    private val _isWatched = MutableLiveData<Boolean>()
    val isWatched: LiveData<Boolean> get() = _isWatched

    fun modifyMovie(movie: Movie){
        viewModelScope.launch {
            val movieEntity = movie.toEntity()
            if(movieEntity.isFavorite ||movieEntity.isWatched){
                mediaDBRepository.addEditMovie(movieEntity)
            }else{
                mediaDBRepository.removeMovie(movieEntity)
            }
        }
    }

    fun modifySeries(series: Series){
        viewModelScope.launch {
            val seriesEntity = series.toEntity()
            if(seriesEntity.isFavorite ||seriesEntity.isWatched){
                mediaDBRepository.addEditSeries(seriesEntity)

            }else{
                mediaDBRepository.removeSeries(seriesEntity)
            }
        }
    }

    // ✅ משתנה למעקב אחרי הרשימה הפעילה (סרטים / סדרות)
    enum class ListType { MOVIES, SERIES }

    private val _currentListType = MutableStateFlow(ListType.MOVIES)
    val currentListType: StateFlow<ListType> get() = _currentListType

    private var isSearching = false // ✅ עוקבים אחרי מצב החיפוש

    private val _currentMovies = MutableStateFlow<List<Movie>>(emptyList())
    val currentMovies: StateFlow<List<Movie>> get() = _currentMovies

    private val _currentSeries = MutableStateFlow<List<Series>>(emptyList())
    val currentSeries: StateFlow<List<Series>> get() = _currentSeries

    private val _currentMoviesPageFlow = MutableStateFlow(1) // ✅ מספר עמוד לסרטים
    val currentMoviesPageFlow: StateFlow<Int> get() = _currentMoviesPageFlow

    private val _currentSeriesPageFlow = MutableStateFlow(1) // ✅ מספר עמוד לסדרות
    val currentSeriesPageFlow: StateFlow<Int> get() = _currentSeriesPageFlow

    private var totalMoviePages = 1
    private var totalSeriesPages = 1

    val topRatedMovies: Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = { MoviesPagingSource(moviesRepository.provideApiService(), Constants.API_KEY) }
    ).flow.cachedIn(viewModelScope)

    val mostPopularSeries = seriesRepository.getMostPopularSeries()
        .flow
        .cachedIn(viewModelScope)

    init {
        getPopularMovies()
        getPopularSeries()
    }

    // ✅ עדכון סוג הרשימה הפעילה
    fun setCurrentListType(type: ListType) {
        _currentListType.value = type
    }

    fun getPopularMovies(page: Int = 1) {
        if (isSearching) return // ❌ לא נאפשר עימוד בזמן חיפוש

        viewModelScope.launch {
            try {
                val response = moviesRepository.getPopularMovies(Constants.API_KEY, page)
                if (response.isSuccessful) {
                    val movieResponse = response.body()
                    val moviesList = movieResponse?.results ?: emptyList()
                    totalMoviePages = movieResponse?.total_pages ?: 1

                    _currentMovies.value = moviesList // ✅ מחליפים רשימה במקום לצבור נתונים
                    _currentMoviesPageFlow.value = page // ✅ עדכון מספר העמוד
                }
            } catch (e: Exception) {
                _currentMovies.value = emptyList()
            }
        }
    }

    fun getPopularSeries(page: Int = 1) {
        if (isSearching) return

        viewModelScope.launch {
            try {
                println("🔹 Fetching series page: $page")
                val response = seriesRepository.getPopularSeries(page)
                if (response.isSuccessful) {
                    val seriesResponse = response.body()
                    val seriesList = seriesResponse?.results ?: emptyList()
                    totalSeriesPages = seriesResponse?.total_pages ?: 1

                    println("✅ Loaded ${seriesList.size} series for page: $page")

                    if (seriesList.isNotEmpty()) {
                        _currentSeries.value = seriesList
                        println("🔄 Total series in list: ${_currentSeries.value.size}")
                        _currentSeriesPageFlow.value = page

                        println("🔄 UI updated with ${_currentSeries.value.size} series")
                    } else {
                        println("❌ No series found for page: $page")
                    }
                } else {
                    println("❌ Error loading series page $page: ${response.message()}")
                }
            } catch (e: Exception) {
                println("❌ Exception in fetchTopRatedSeries: ${e.message}")
                _currentSeries.value = emptyList()
            }
        }
    }







    fun searchMoviesAndSeries(query: String) {
        viewModelScope.launch {
            if (query.isEmpty()) {
                isSearching = false
                getPopularMovies(1) // ✅ חזרה לעימוד רגיל
                getPopularSeries(1)
                return@launch
            }

            isSearching = true
            try {
                val moviesResponse = moviesRepository.searchMovies(query)
                val seriesResponse = seriesRepository.searchSeries(query)

                if (moviesResponse.isSuccessful) {
                    _currentMovies.value = moviesResponse.body()?.results ?: emptyList()
                }

                if (seriesResponse.isSuccessful) {
                    _currentSeries.value = seriesResponse.body()?.results ?: emptyList()
                }
            } catch (e: Exception) {
                _currentMovies.value = emptyList()
                _currentSeries.value = emptyList()
            }
        }
    }
    // ✅ ניווט דינמי בהתאם לרשימה הפעילה
    fun loadNextPage() {
        if (!isSearching) {
            when (_currentListType.value) {
                ListType.MOVIES -> loadNextMoviePage()
                ListType.SERIES -> loadNextSeriesPage()
            }
        }
    }

    fun loadPreviousPage() {
        if (!isSearching) {
            when (_currentListType.value) {
                ListType.MOVIES -> loadPreviousMoviePage()
                ListType.SERIES -> loadPreviousSeriesPage()
            }
        }
    }
    // ✅ ניווט בין דפי הסרטים
    fun loadNextMoviePage() {
        if (!isSearching && _currentMoviesPageFlow.value < totalMoviePages) {
            getPopularMovies(_currentMoviesPageFlow.value + 1)
        }
    }

    fun loadPreviousMoviePage() {
        if (!isSearching && _currentMoviesPageFlow.value > 1) {
            getPopularMovies(_currentMoviesPageFlow.value - 1)
        }
    }

    // ✅ ניווט בין דפי הסדרות
    fun loadNextSeriesPage() {
        if (!isSearching && _currentSeriesPageFlow.value < totalSeriesPages) {
            getPopularSeries(_currentSeriesPageFlow.value + 1)
        }
    }

    fun loadPreviousSeriesPage() {
        if (!isSearching && _currentSeriesPageFlow.value > 1) {
            getPopularSeries(_currentSeriesPageFlow.value - 1)
        }
    }



}

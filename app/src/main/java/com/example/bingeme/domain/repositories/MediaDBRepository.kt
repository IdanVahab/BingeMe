package com.example.bingeme.domain.repositories

import android.util.Log
import com.example.bingeme.data.local.dao.MediaDao
import com.example.bingeme.data.local.entities.MovieEntity
import com.example.bingeme.data.local.entities.SeriesEntity
import com.example.bingeme.data.models.Movie
import com.example.bingeme.data.models.Series
import com.example.bingeme.utils.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MediaDBRepository @Inject constructor(
    private val mediaDao: MediaDao
) {

    suspend fun addMovie(movieEntity: MovieEntity) {
        mediaDao.insertMovie(movieEntity)
    }
    suspend fun addSeries(series: SeriesEntity) {
        Log.d("WatchlistRepository", "Adding series to watchlist: ${series.id} ")
        mediaDao.insertSeries(series)
    }


    suspend fun removeMovie(movieEntity: MovieEntity) {
        mediaDao.deleteMovie(movieEntity)
    }
    suspend fun removeSeries(series: SeriesEntity) = mediaDao.deleteSeries(series)


    fun getFavoriteMovies(): Flow<List<Movie>> {
        return mediaDao.getAllFavoriteMovies().map { entities ->
            entities.map { it.toModel() } }
    }
    fun getFavoriteSeries(): Flow<List<Series>> {
        return mediaDao.getAllFavoriteSeries().map { entities ->
            entities.map { it.toModel() } }
    }

    fun getWatchedMovies(): Flow<List<Movie>> {
        return mediaDao.getWatchedMovies().map { entities ->
            entities.map { it.toModel() }
        }
    } fun getWatchedSeries(): Flow<List<Series>> {
        return mediaDao.getWatchedSeries().map { entities ->
            entities.map { it.toModel() }
        }
    }

    suspend fun isMovieInWatchlist(movieId: Int): Boolean {
        val exists = mediaDao.isMovieInWatchlist(movieId)
        Log.d("WatchlistRepository", "Checking if movie ($movieId) is in watchlist: $exists")
        return exists
    }
    suspend fun isSeriesInWatchlist(seriesId: Int): Boolean {
        val exists = mediaDao.isSeriesInWatchlist(seriesId)
        Log.d("WatchlistRepository", "Checking if series ($seriesId) is in watchlist: $exists")
        return exists
    }


    suspend fun isMovieWatched(movieId: Int): Boolean {
        return mediaDao.isMovieWatched(movieId)
    }
    suspend fun isSeriesWatched(seriesId: Int): Boolean {
        return mediaDao.isSeriesWatched(seriesId)
    }


    suspend fun addMovieToWatchlist(movie: MovieEntity) {
        if (!mediaDao.isMovieInWatchlist(movie.id)) {
            mediaDao.insertMovie(movie)
        }
    }
    suspend fun addSeriesToWatchlist(series: SeriesEntity) {
        if (!mediaDao.isSeriesInWatchlist(series.id)) {
            mediaDao.insertSeries(series)
        }
    }


    suspend fun removeMovieFromWatchlist(movie: MovieEntity) {
        mediaDao.deleteMovie(movie)
    }
    suspend fun removeSeriesFromWatchlist(series: SeriesEntity) {
        mediaDao.deleteSeries(series)
    }








    suspend fun markMovieAsWatched(movieId: Int) {
        mediaDao.updateMovieWatchedStatus(movieId, true)
    }
    suspend fun markSeriesAsWatched(seriesId: Int) {
        mediaDao.updateSeriesWatchedStatus(seriesId, true)
    }


    suspend fun unmarkMovieAsWatched(movieId: Int) {
        mediaDao.updateMovieWatchedStatus(movieId, false)
    }
    suspend fun unmarkSeriesAsWatched(seriesId: Int) {
        mediaDao.updateSeriesWatchedStatus(seriesId, false)
    }
}

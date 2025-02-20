package com.example.bingeme.domain.repositories

import com.example.bingeme.data.local.dao.WatchlistDao
import com.example.bingeme.data.local.entities.MovieEntity
import com.example.bingeme.data.local.entities.SeriesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WatchedRepository @Inject constructor(
    private val dao: WatchlistDao
) {

    fun getAllWatchedMovies(): Flow<List<MovieEntity>> {
        return dao.getWatchedMovies()
    }

    fun getAllWatchedSeries(): Flow<List<SeriesEntity>> {
        return dao.getWatchedSeries()
    }

    suspend fun removeMovie(movieEntity: MovieEntity) {
        dao.updateMovieWatchedStatus(movieEntity.id, false)
    }

    suspend fun removeSeries(seriesEntity: SeriesEntity) {
        dao.updateSeriesWatchedStatus(seriesEntity.id, false)
    }
}

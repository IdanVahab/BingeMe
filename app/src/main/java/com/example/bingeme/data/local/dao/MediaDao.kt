package com.example.bingeme.data.local.dao

import androidx.room.*
import com.example.bingeme.data.local.entities.MovieEntity
import com.example.bingeme.data.local.entities.SeriesEntity
import kotlinx.coroutines.flow.Flow

/**
 * The WatchlistDao interface defines database operations for managing
 * movies and series in the user's watchlist. This includes adding,
 * deleting, and querying items from the local Room database.
 */
@Dao
interface MediaDao {

//Add or edit
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeries(series: SeriesEntity)

    @Delete
    suspend fun deleteMovie(movie: MovieEntity)
    @Delete
    suspend fun deleteSeries(series: SeriesEntity)

    @Query("SELECT * FROM movies WHERE isFavorite = 1")
    fun getAllFavoriteMovies(): Flow<List<MovieEntity>>
    @Query("SELECT * FROM series WHERE isFavorite = 1")
    fun getAllFavoriteSeries(): Flow<List<SeriesEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM movies WHERE id = :movieId AND isFavorite = 1)")
    suspend fun isMovieInWatchlist(movieId: Int): Boolean
    @Query("SELECT EXISTS(SELECT 1 FROM series WHERE id = :seriesId AND isFavorite = 1)")
    suspend fun isSeriesInWatchlist(seriesId: Int): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM movies WHERE id = :movieId AND isWatched = 1)")
    suspend fun isMovieWatched(movieId: Int): Boolean
    @Query("SELECT EXISTS(SELECT 1 FROM series WHERE id = :seriesId AND isWatched = 1)")
    suspend fun isSeriesWatched(seriesId: Int): Boolean

    @Query("SELECT * FROM movies WHERE isWatched = 1")
    fun getWatchedMovies(): Flow<List<MovieEntity>>
    @Query("SELECT * FROM series WHERE isWatched = 1")
    fun getWatchedSeries(): Flow<List<SeriesEntity>>

    @Query("UPDATE movies SET isWatched = :watched WHERE id = :movieId")
    suspend fun updateMovieWatchedStatus(movieId: Int, watched: Boolean)
    @Query("UPDATE series SET isWatched = :watched WHERE id = :seriesId")
    suspend fun updateSeriesWatchedStatus(seriesId: Int, watched: Boolean)

    @Query("SELECT id FROM series WHERE isFavorite = 1")
    suspend fun getFavoriteSeriesIds(): List<Int>
    @Query("SELECT id FROM movies WHERE isFavorite = 1")
    suspend fun getFavoriteMoviesIds(): List<Int>


    @Query("SELECT id FROM series WHERE isWatched = 1")
    suspend fun getWatchedSeriesIds(): List<Int>
    @Query("SELECT id FROM movies WHERE isWatched = 1")
    suspend fun getWatchedMoviesIds(): List<Int>
}
